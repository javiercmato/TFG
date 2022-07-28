package es.udc.fic.tfg.backendtfg.ingredients.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityAlreadyExistsException;
import es.udc.fic.tfg.backendtfg.common.domain.jwt.JwtData;
import es.udc.fic.tfg.backendtfg.common.infrastructure.controllers.CommonControllerAdvice;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.ingredients.application.services.IngredientService;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientRepository;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientTypeRepository;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.controllers.IngredientController;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.conversors.IngredientConversor;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.CreateIngredientParamsDTO;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.UserController;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.AuthenticatedUserDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.SignUpParamsDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static es.udc.fic.tfg.backendtfg.common.infrastructure.security.JwtFilter.AUTH_TOKEN_PREFIX;
import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IngredientControllerTest {
    private static final String API_ENDPOINT = "/api/ingredients";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Locale locale = Locale.getDefault();
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserController userController;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientTypeRepository ingredientTypeRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private IngredientController ingredientController;
    @Autowired
    private MessageSource messageSource;
    
    
    /* ************************* MÉTODOS AUXILIARES ************************* */
    /** Registra un usuario válido. */
    private AuthenticatedUserDTO registerValidUser(String nickname) throws EntityAlreadyExistsException {
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
        user.setAvatar(new byte[0]);
    
        // Generar el DTO para registrar al usuario
        SignUpParamsDTO signUpParamsDTO = new SignUpParamsDTO();
        signUpParamsDTO.setName(user.getName());
        signUpParamsDTO.setSurname(user.getSurname());
        signUpParamsDTO.setNickname(user.getNickname());
        signUpParamsDTO.setPassword(user.getPassword());
        signUpParamsDTO.setEmail(user.getEmail());
        
        // Registrar usuario
        return userController.signUp(signUpParamsDTO).getBody();
    }
    
    /** Registra un tipo de ingrediente válido. */
    private IngredientType registerIngredientType() {
        IngredientType type = new IngredientType();
        type.setName(DEFAULT_INGREDIENTTYPE_NAME);
        
        return ingredientTypeRepository.save(type);
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
        System.arraycopy(args, 0, values, 1, args.length);
        return messageSource.getMessage(
                propertyName,
                args,
                propertyName,
                locale
        );
    }
    
    
    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenCreateIngredient_thenIngredientIsCreated() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO authUserDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(authUserDTO.getServiceToken());
        IngredientType ingredientType = registerIngredientType();
        CreateIngredientParamsDTO paramsDTO = new CreateIngredientParamsDTO();
        paramsDTO.setName(DEFAULT_INGREDIENT_NAME);
        paramsDTO.setIngredientTypeID(ingredientType.getId());
        paramsDTO.setCreatorID(authUserDTO.getUserDTO().getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
            post(endpointAddress)
                .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(encodedBodyContent)
                // Valores anotados como @RequestAttribute
                .requestAttr("userID", jwtData.getUserID())
                .requestAttr("token", jwtData.toString())
        );
        
        // Comprobar resultados
        Ingredient expectedIngredient = ingredientRepository.findByNameLikeIgnoreCase(paramsDTO.getName()).get();
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(IngredientConversor.toIngredientDTO(expectedIngredient));
        action.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenCreateIngredient_AsOtherUser_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO authUserDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(authUserDTO.getServiceToken());
        IngredientType ingredientType = registerIngredientType();
        CreateIngredientParamsDTO paramsDTO = new CreateIngredientParamsDTO();
        paramsDTO.setName(DEFAULT_INGREDIENT_NAME);
        paramsDTO.setIngredientTypeID(ingredientType.getId());
        paramsDTO.setCreatorID(authUserDTO.getUserDTO().getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", UUID.randomUUID())
                        .requestAttr("token", jwtData.toString())
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
        
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isForbidden())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
}
