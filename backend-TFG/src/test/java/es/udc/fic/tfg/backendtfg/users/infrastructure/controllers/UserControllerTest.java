package es.udc.fic.tfg.backendtfg.users.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.common.domain.jwt.JwtData;
import es.udc.fic.tfg.backendtfg.common.infrastructure.controllers.CommonControllerAdvice;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.users.application.services.UserService;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static es.udc.fic.tfg.backendtfg.common.infrastructure.security.JwtFilter.AUTH_TOKEN_PREFIX;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.PNG_EXTENSION;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.loadImageFromResourceName;
import static es.udc.fic.tfg.backendtfg.utils.UserTestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserControllerTest {
    private static final String API_ENDPOINT = "/api/users";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Locale locale = Locale.getDefault();
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private MessageSource messageSource;
    
    
    /* ************************* MÉTODOS AUXILIARES ************************* */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /** Genera datos de un usuario válido. */
    private User generateValidUser(String nickname) {
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(DEFAULT_PASSWORD);
        user.setName(DEFAULT_NAME);
        user.setSurname(DEFAULT_SURNAME);
        user.setEmail(nickname.toLowerCase() + DEFAULT_EMAIL_DOMAIN);
        user.setRole(UserRole.USER);
        user.setBannedByAdmin(false);
        user.setRegisterDate(LocalDateTime.now());
        user.setPrivateLists(Collections.emptySet());
        
        // Cargar imagen por defecto
        byte[] avatarBytes = loadImageFromResourceName(DEFAULT_AVATAR_NAME, PNG_EXTENSION);
        user.setAvatar(avatarBytes);
        
        return user;
    }
    
    /** Registra el usuario recibido en el sistema y devuelve sus datos y el token de acceso */
    private AuthenticatedUserDTO createAuthenticatedUser(User user) throws IncorrectLoginException {
        // Guardar al usuario en la BD
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userRepository.save(user);
        
        // Generar el DTO con los datos del usuario recién creado
        LoginParamsDTO loginParamsDTO = new LoginParamsDTO();
        loginParamsDTO.setNickname(user.getNickname());
        loginParamsDTO.setPassword(DEFAULT_PASSWORD);
        
        // Iniciar sesión para obtener los datos del usuario y el token
        return userController.login(loginParamsDTO);
    }
    
    private SignUpParamsDTO generateSignUpParamsDtoFromUser(User user) {
        SignUpParamsDTO dto = new SignUpParamsDTO();
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setNickname(user.getNickname());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setAvatar(
            Base64.getEncoder().encodeToString(user.getAvatar())
        );
        
        return dto;
    }
    
    /** Recupera el texto asociado a la propiedad recibida a partir del fichero de I18N en el idioma indicado. */
    private String getI18NExceptionMessage(String propertyName, Locale locale) {
        return messageSource.getMessage(
                propertyName,
                null,
                propertyName,
                locale
        );
    }
    
    /** Recupera el texto asociado a la propiedad recibida con los parámetros recibidos a partir del fichero de I18N en el idioma indicado. */
    private String getI18NExceptionMessageWithParams(String propertyName, Locale locale, Object[] args, Class exceptionClass) {
        String exceptionMessage = messageSource.getMessage(
                exceptionClass.getSimpleName(), null, exceptionClass.getSimpleName(), locale
        );
        // Añadir el mensaje traducido al principio del array de argumentos a traducir
        Object[] values = new Object[args.length + 1];
        for (int i = 1; i <= args.length; i++) {
            values[i] = args[i-1];
        }
        return messageSource.getMessage(
                propertyName,
                args,
                propertyName,
                locale
        );
    }
    
    
    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenSignUp_thenNewUserIsCreated() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        SignUpParamsDTO paramsDTO = generateSignUpParamsDtoFromUser(validUser);     // Parámetros para registrarse
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/register";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions signUpAction = mockMvc.perform(
            post(endpointAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedBodyContent)
        );
        
        // Comprobar resultados
        //AuthenticatedUserDTO expectedDTO = createAuthenticatedUser(validUser);      // Respuesta deseada
        //String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedDTO);
        signUpAction
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // FIXME: Ver como evitar comparar contraseña sin cifrar al registrar usuario (SignUpParamsDTO)
        // y contraseña cifrada al iniciar sesión (AuthenticatedUserDTO)
        //    .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenSignUpTwice_thenBadRequest_becauseEntityAlreadyExistsException() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        SignUpParamsDTO paramsDTO = generateSignUpParamsDtoFromUser(validUser);     // Parámetros para registrarse
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/register";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions signUpAction = mockMvc.perform(
            post(endpointAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedBodyContent)
        );
        ResultActions signUpTwiceAction = mockMvc.perform(
            post(endpointAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedBodyContent)
        );
        
        // Comprobar resultados
        signUpAction
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        signUpTwiceAction
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    
    @Test
    void whenLogin_thenReturnAuthenticatedUserDTO() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        LoginParamsDTO paramsDTO = new LoginParamsDTO(nickname, DEFAULT_PASSWORD);
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
    
        // Comprobar resultados
        //String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(authUserDTO);
        loginAction.andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON));
                   //.andExpect(content().string(encodedResponseBodyContent));
        
    }
    
    
    @Test
    void whenLoginWithIncorrectPassword_thenBadRequest_becauseIncorrectLoginException() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        LoginParamsDTO paramsDTO = new LoginParamsDTO(nickname, DEFAULT_PASSWORD + 'X');
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
        );
        String errorMessage = getI18NExceptionMessage(UserController.INCORRECT_LOGIN_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        loginAction.andExpect(status().isBadRequest())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(content().json(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenLoginNonExistentUser_thenBadRequest_becauseIncorrectLoginException() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        LoginParamsDTO paramsDTO = new LoginParamsDTO(nickname, DEFAULT_PASSWORD);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
        );
        String errorMessage = getI18NExceptionMessage(UserController.INCORRECT_LOGIN_EXCEPTION_KEY, locale);
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        loginAction.andExpect(status().isBadRequest())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(content().json(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenLoginWithToken_thenReturnAuthenticatedUserDTO() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        JwtData jwtData = jwtGenerator.extractInfo(authUserDTO.getServiceToken());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login/token";
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
        );
        
        // Comprobar resultados
        //String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(authUserDTO);
        loginAction.andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //.andExpect(content().string(encodedResponseBodyContent));
        
    }
    
    
    @Test
    void whenLoginWithTokenNonExistentUser_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);      // Registra un usuario y obtiene el DTO respuesta
        JwtData jwtData = jwtGenerator.extractInfo(authUserDTO.getServiceToken());
        userRepository.deleteById(authUserDTO.getUserDTO().getUserID());            // Borrar al usuario recien creado para que no se pueda encontrar
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/login/token";
        ResultActions loginAction = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale,
                new Object[] {User.class.getName(), jwtData.getUserID()},
                User.class
        );
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        loginAction.andExpect(status().isNotFound())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenChangePassword_thenNoContent_andPasswordIsChanged() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);
        UUID userID = authUserDTO.getUserDTO().getUserID();
        ChangePasswordParamsDTO paramsDTO = new ChangePasswordParamsDTO(DEFAULT_PASSWORD, DEFAULT_PASSWORD + "X");
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + userID.toString() + "/changePassword";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions changePasswordAction = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        
        // Comprobar resultados
        changePasswordAction.andExpect(status().isNoContent());
    }
    
    
    @Test
    void whenChangePasswordNoExistentUser_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);
        UUID userID = authUserDTO.getUserDTO().getUserID();
        ChangePasswordParamsDTO paramsDTO = new ChangePasswordParamsDTO(DEFAULT_PASSWORD, DEFAULT_PASSWORD + "X");
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + NON_EXISTENT_UUID + "/changePassword";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions changePasswordAction = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", NON_EXISTENT_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale,
                new Object[] {User.class.getName(), NON_EXISTENT_UUID},
                User.class
        );
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        changePasswordAction.andExpect(status().isNotFound())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenChangePasswordToOtherUser_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        User currentUser = generateValidUser("currentUser");
        AuthenticatedUserDTO currentUserDTO = createAuthenticatedUser(currentUser);
        User targetUser = generateValidUser("targetUser");
        AuthenticatedUserDTO targetUserDTO = createAuthenticatedUser(targetUser);
        UUID currentUserID = currentUserDTO.getUserDTO().getUserID();
        UUID targetUserID = targetUserDTO.getUserDTO().getUserID();
        ChangePasswordParamsDTO paramsDTO = new ChangePasswordParamsDTO(DEFAULT_PASSWORD, DEFAULT_PASSWORD + "X");
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + targetUserID.toString() + "/changePassword";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions changePasswordAction = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + currentUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", currentUserID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        changePasswordAction.andExpect(status().isForbidden())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenChangePasswordIncorrectly_thenBadRequest_becauseIncorrectPasswordException() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);
        UUID userID = authUserDTO.getUserDTO().getUserID();
        ChangePasswordParamsDTO paramsDTO = new ChangePasswordParamsDTO();
        paramsDTO.setOldPassword(DEFAULT_PASSWORD + "1234");
        paramsDTO.setNewPassword(DEFAULT_PASSWORD + "5678");
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + userID.toString() + "/changePassword";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions changePasswordAction = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        String errorMessage = getI18NExceptionMessage(UserController.INCORRECT_PASSWORD_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        changePasswordAction.andExpect(status().isBadRequest())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenUpdateProfile_thenReturnUserDTO() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(validUser);
        UUID userID = authUserDTO.getUserDTO().getUserID();
        User originalUser = userService.findUserById(userID);
        UpdateProfileParamsDTO paramsDTO = new UpdateProfileParamsDTO();
        paramsDTO.setName(originalUser.getName() + "X");
        paramsDTO.setSurname(originalUser.getSurname() + "X");
        paramsDTO.setEmail("X" + originalUser.getEmail());
        paramsDTO.setAvatar(Base64.getEncoder()
            .encodeToString(
                loadImageFromResourceName("updated_user_avatar.png", PNG_EXTENSION)
            )
        );
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + userID.toString();
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions updateProfileAction = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
    
        // Comprobar resultados
        User updatedUser = userService.findUserById(userID);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(UserConversor.toUserDTO(updatedUser));
        updateProfileAction.andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenUpdateProfileToOtherUser_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        User currentUser = generateValidUser("currentUser");
        User targetUser = generateValidUser("targetUser");
        AuthenticatedUserDTO currentUserDTO = createAuthenticatedUser(currentUser);
        AuthenticatedUserDTO targetUserDTO = createAuthenticatedUser(targetUser);
        UUID currentUserID = currentUserDTO.getUserDTO().getUserID();
        UUID targetUserID = targetUserDTO.getUserDTO().getUserID();
        User currentUserOriginal = userService.findUserById(currentUserID);
        UpdateProfileParamsDTO paramsDTO = new UpdateProfileParamsDTO();
        paramsDTO.setName(currentUserOriginal.getName() + "X");
        paramsDTO.setSurname(currentUserOriginal.getSurname() + "X");
        paramsDTO.setEmail("X" + currentUserOriginal.getEmail());
        paramsDTO.setAvatar(Base64.getEncoder()
            .encodeToString(
                loadImageFromResourceName("updated_user_avatar.png", PNG_EXTENSION)
            )
        );
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + targetUserID.toString();
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions updateProfileAction = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + currentUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", currentUserID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        updateProfileAction.andExpect(status().isForbidden())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenDeleteUser_thenNoContent() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(generateValidUser(nickname));
        UUID userID = authUserDTO.getUserDTO().getUserID();
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + userID.toString();
        ResultActions deleteUserAction = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", userID)
        );
    
        // Comprobar resultados
        deleteUserAction.andExpect(status().isNoContent());
    }
    
    
    @Test
    void whenDeleteAnotherUser__thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        User currentUser = generateValidUser("currentUser");
        User targetUser = generateValidUser("targetUser");
        AuthenticatedUserDTO currentUserDTO = createAuthenticatedUser(currentUser);
        AuthenticatedUserDTO targetUserDTO = createAuthenticatedUser(targetUser);
        UUID currentUserID = currentUserDTO.getUserDTO().getUserID();
        UUID targetUserID = targetUserDTO.getUserDTO().getUserID();
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + targetUserID.toString();
        ResultActions deleteUserAction = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + currentUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", currentUserID)
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        deleteUserAction.andExpect(status().isForbidden())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenFindUserByID_thenReturnUserDTO() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(generateValidUser(nickname));
        UUID userID = authUserDTO.getUserDTO().getUserID();
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + userID.toString();
        ResultActions findUserAction = mockMvc.perform(
                get(endpointAddress)
        );
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(authUserDTO.getUserDTO());
        findUserAction.andExpect(status().isOk())
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenFindUserByNickname_thenReturnUserDTO() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        AuthenticatedUserDTO authUserDTO = createAuthenticatedUser(generateValidUser(nickname));
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/?nickname=" + nickname;
        ResultActions findUserAction = mockMvc.perform(
                get(endpointAddress)
        );
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(authUserDTO.getUserDTO());
        findUserAction.andExpect(status().isOk())
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
    @Test
    void whenBanUserAsAdmin_thenReturnUserDTO() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        AuthenticatedUserDTO targetUserDTO = createAuthenticatedUser(generateValidUser(nickname));
        User admin = userRepository.findByNicknameIgnoreCase(ADMIN_NICKNAME).get();
        AuthenticatedUserDTO adminDTO = createAuthenticatedUser(admin);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/admin/ban/" + targetUserDTO.getUserDTO().getUserID();
        ResultActions banUserAsAdminAction = mockMvc.perform(
                put(endpointAddress)
                    .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + adminDTO.getServiceToken())
                    // Valores anotados como @RequestAttribute
                    .requestAttr("userID", admin)
        );
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(true);
        banUserAsAdminAction.andExpect(status().isOk())
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenBanUserAsAdmin_andCurrentUserIsNotAdmin_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        String targetNickname = "TargetUser";
        String currentUserNickname = "CurrentUser";
        AuthenticatedUserDTO targetUserDTO = createAuthenticatedUser(generateValidUser(targetNickname));
        AuthenticatedUserDTO notAdminUserDTO = createAuthenticatedUser(generateValidUser(currentUserNickname));
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/admin/ban/" + targetUserDTO.getUserDTO().getUserID();
        ResultActions banUserAsAdminAction = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + notAdminUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", notAdminUserDTO.getUserDTO().getUserID())
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        banUserAsAdminAction.andExpect(status().isForbidden())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(encodedResponseBodyContent));
    }
    
}
