package es.udc.fic.tfg.backendtfg.recipes.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityAlreadyExistsException;
import es.udc.fic.tfg.backendtfg.common.domain.jwt.JwtData;
import es.udc.fic.tfg.backendtfg.common.infrastructure.controllers.CommonControllerAdvice;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.recipes.application.RecipeService;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.CategoryRepository;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.CategoryConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.CategoryDTO;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.CreateCategoryParamsDTO;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.UserController;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RecipeControllerTest {
    private static final String API_ENDPOINT = "/api/recipes";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Locale locale = Locale.getDefault();
    
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserController userController;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private RecipeService recipeService;
    
    
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
    
    /** Inicia sesión como administrador y devuelve sus datos y el token de acceso */
    private AuthenticatedUserDTO loginAsAdmin() throws IncorrectLoginException {
        // Generar el DTO con los datos del usuario recién creado
        LoginParamsDTO loginParamsDTO = new LoginParamsDTO();
        loginParamsDTO.setNickname(ADMIN_NICKNAME);
        loginParamsDTO.setPassword(ADMIN_PASSWORD);
        
        // Iniciar sesión para obtener los datos del usuario y el token
        return userController.login(loginParamsDTO);
    }
    
    /** Registra una categoría válida */
    private Category registerCategory() {
        Category category = new Category();
        category.setName(DEFAULT_CATEGORY_NAME);
        
        return categoryRepo.save(category);
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
    void whenCreateCategory_thenCategoryIsCreated() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO adminDTO = loginAsAdmin();
        JwtData jwtData = jwtGenerator.extractInfo(adminDTO.getServiceToken());
        CreateCategoryParamsDTO paramsDTO = new CreateCategoryParamsDTO();
        paramsDTO.setName(DEFAULT_CATEGORY_NAME);
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/categories";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + adminDTO.getServiceToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        
        // Comprobar resultados
        Category expectedResponse = categoryRepo.findByNameIgnoreCase(DEFAULT_CATEGORY_NAME).get();
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenCreateCategory_NotAsAdmin_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO authUserDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(authUserDTO.getServiceToken());
        Category category = registerCategory();
        CreateCategoryParamsDTO paramsDTO = new CreateCategoryParamsDTO();
        paramsDTO.setName(DEFAULT_CATEGORY_NAME);
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/categories";
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
    
    @Test
    void whenCreateCategoryTwice_thenBadRequest_becauseEntityAlreadyExistsException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO authUserDTO = loginAsAdmin();
        JwtData jwtData = jwtGenerator.extractInfo(authUserDTO.getServiceToken());
        CreateCategoryParamsDTO paramsDTO = new CreateCategoryParamsDTO();
        paramsDTO.setName(DEFAULT_CATEGORY_NAME);
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/categories";
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions firstAction = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        ResultActions secondAction = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + authUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_ALREADY_EXISTS_EXCEPTION_KEY,
                locale,
                new Object[] {Category.class.getSimpleName(), DEFAULT_CATEGORY_NAME},
                Category.class
        );
        
        // Comprobar resultados
        Category category = categoryRepo.findByNameIgnoreCase(DEFAULT_CATEGORY_NAME).get();
        CategoryDTO expectedResponse = CategoryConversor.toCategoryDTO(category);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        firstAction.andExpect(status().isCreated())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(content().string(encodedResponseBodyContent));
        String encodedResponseErrorBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        secondAction.andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(encodedResponseErrorBodyContent));
    }
    
    @Test
    void whenFindAllCategories_thenCategoriesListIsRetrieved() throws Exception {
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/categories";
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
        );
        
        // Comprobar resultados
        List<Category> categories = recipeService.getAllCategories();
        List<CategoryDTO> expectedResponse = CategoryConversor.toCategoryListDTO(categories);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    
}
