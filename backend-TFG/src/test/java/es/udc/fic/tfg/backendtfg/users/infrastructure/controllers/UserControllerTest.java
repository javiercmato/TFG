package es.udc.fic.tfg.backendtfg.users.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.ResourceBannedByAdministratorException;
import es.udc.fic.tfg.backendtfg.users.application.services.UserService;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;

import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.PNG_EXTENSION;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.loadImageFromResourceName;
import static es.udc.fic.tfg.backendtfg.utils.UserTestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private UserControllerUtils userControllerUtils;
    
    
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
        
        // Cargar imagen por defecto
        byte[] avatarBytes = loadImageFromResourceName(DEFAULT_AVATAR_NAME, PNG_EXTENSION);
        user.setAvatar(avatarBytes);
        
        return user;
    }
    
    /** Registra el usuario recibido en el sistema y devuelve sus datos y el token de acceso */
    private AuthenticatedUserDTO createAuthenticatedUser(User user)
            throws IncorrectLoginException, ResourceBannedByAdministratorException {
        // Guardar al usuario en la BD
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userRepository.save(user);
        
        // Generar el DTO con los datos del usuario recién creado
        LoginParamsDTO loginParamsDTO = new LoginParamsDTO();
        loginParamsDTO.setNickname(user.getNickname());
        loginParamsDTO.setPassword(DEFAULT_PASSWORD);
        
        // Iniciar sesión para obtener los datos del usuario y el token
        AuthenticatedUserDTO dto = userController.login(loginParamsDTO);
        userRepository.delete(user);
        return dto;
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
    
    
    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenSignUp_thenNewUserIsCreated() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        SignUpParamsDTO paramsDTO = generateSignUpParamsDtoFromUser(validUser);     // Parámetros para registrarse
        AuthenticatedUserDTO expectedDTO = createAuthenticatedUser(validUser);      // Respuesta deseada
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/register";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedDTO);
        ResultActions performAction = mockMvc.perform(
            post(endpointAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedBodyContent)
        );
        
        // Comprobar resultados
        performAction.andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // FIXME: Ver como evitar comparar contraseña sin cifrar al registrar usuario (SignUpParamsDTO)
        // y contraseña cifrada al iniciar sesión (AuthenticatedUserDTO)
        //    .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenSignUpTwice_thenEntityAlreadyExistsException() throws Exception {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        SignUpParamsDTO paramsDTO = generateSignUpParamsDtoFromUser(validUser);     // Parámetros para registrarse
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/register";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions successfulSignUp = mockMvc.perform(
            post(endpointAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedBodyContent)
        );
        ResultActions exceptionalSignUp = mockMvc.perform(
            post(endpointAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedBodyContent)
        );
        
        // Comprobar resultados
        successfulSignUp
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        exceptionalSignUp
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
