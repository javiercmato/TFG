package es.udc.fic.tfg.backendtfg.social.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityAlreadyExistsException;
import es.udc.fic.tfg.backendtfg.common.domain.jwt.JwtData;
import es.udc.fic.tfg.backendtfg.ingredients.application.IngredientService;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientTypeRepository;
import es.udc.fic.tfg.backendtfg.recipes.application.RecipeService;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.*;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.social.application.SocialService;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.domain.entities.CommentID;
import es.udc.fic.tfg.backendtfg.social.domain.repositories.CommentRepository;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.CreateCommentParamsDTO;
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
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.PNG_EXTENSION;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.loadImageFromResourceName;
import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SocialControllerTest {
    private static final String API_ENDPOINT = "/api/social";
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
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepo;
    @Autowired
    private IngredientTypeRepository ingredientTypeRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SocialService socialService;
    
    
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
    
    /** Genera un DTO con los parámetros para crear una receta.
     * Faltan los parámetros: steps, pictures e ingredients
     */
    private CreateRecipeParamsDTO generateCreateRecipeParamsDTO(UUID authorID, UUID categoryID) {
        CreateRecipeParamsDTO paramsDTO = new CreateRecipeParamsDTO();
        paramsDTO.setName(DEFAULT_RECIPE_NAME);
        paramsDTO.setDescription(DEFAULT_RECIPE_DESCRIPTION);
        paramsDTO.setDuration(DEFAULT_RECIPE_DURATION);
        paramsDTO.setDiners(DEFAULT_RECIPE_DINERS);
        paramsDTO.setAuthorID(authorID);
        paramsDTO.setCategoryID(categoryID);
        
        return paramsDTO;
    }
    
    
    /** Registra una receta válida sin ingredientes */
    private Recipe registerRecipe(UUID authorID, UUID categoryID) throws Exception {
        // Crear pasos
        List<CreateRecipeStepParamsDTO> stepsParams = new ArrayList<>();
        stepsParams.add(new CreateRecipeStepParamsDTO(1, DEFAULT_RECIPESTEP_TEXT));
        // Crear imágenes
        List<CreateRecipePictureParamsDTO> picturesParams = new ArrayList<>();
        String encodedImage = Base64.getEncoder()
                                    .encodeToString(
                                            loadImageFromResourceName(DEFAULT_RECIPE_IMAGE_1, PNG_EXTENSION));
        picturesParams.add(new CreateRecipePictureParamsDTO(1, encodedImage));
        
        CreateRecipeParamsDTO paramsDTO = generateCreateRecipeParamsDTO(authorID, categoryID);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        paramsDTO.setIngredients(Collections.emptyList());
        
        return recipeService.createRecipe(paramsDTO);
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
    void whenCreateComment_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        String recipeID = recipe.getId().toString();
        CreateCommentParamsDTO paramsDTO = new CreateCommentParamsDTO(DEFAULT_COMMENT_TEXT, jwtData.getUserID());
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/comments/" + recipeID;
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
    
        // Comprobar resultados
        CommentID commentID = new CommentID(jwtData.getUserID(), recipe.getId());
        Comment expectedResponse = commentRepository.findById(commentID).get();
//        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//              .andExpect(content().string(encodedResponseBodyContent));
        
    }
    
    
}
