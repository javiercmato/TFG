package es.udc.fic.tfg.backendtfg.social.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityAlreadyExistsException;
import es.udc.fic.tfg.backendtfg.common.domain.jwt.JwtData;
import es.udc.fic.tfg.backendtfg.common.infrastructure.controllers.CommonControllerAdvice;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.ingredients.application.IngredientService;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.*;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientTypeRepository;
import es.udc.fic.tfg.backendtfg.recipes.application.RecipeService;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.CategoryRepository;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.RecipeConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.social.application.SocialService;
import es.udc.fic.tfg.backendtfg.social.domain.entities.*;
import es.udc.fic.tfg.backendtfg.social.domain.repositories.FollowRepository;
import es.udc.fic.tfg.backendtfg.social.infrastructure.controllers.SocialController;
import es.udc.fic.tfg.backendtfg.social.infrastructure.conversors.*;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SocialControllerTest {
    private static final String API_ENDPOINT = "/api/social";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Locale locale = Locale.getDefault();
    private final int INITIAL_PAGE = 0;
    private final int PAGE_SIZE = 5;
    
    
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
    private IngredientTypeRepository ingredientTypeRepository;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private SocialService socialService;
    @Autowired
    private FollowRepository followRepo;
    
    
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
    
    
    /** Registra una receta válida */
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
        // Crear ingredientes de receta
        IngredientType ingredientType = new IngredientType();
        ingredientType.setName(VALID_INGREDIENTTYPE_NAME);
        ingredientTypeRepository.save(ingredientType);
        Ingredient ingredient = ingredientService.createIngredient(DEFAULT_INGREDIENT_NAME, ingredientType.getId(), authorID);
        List<CreateRecipeIngredientParamsDTO> ingredientsParams = new ArrayList<>();
        ingredientsParams.add(new CreateRecipeIngredientParamsDTO(ingredient.getId(), "1", MeasureUnit.UNIDAD.toString()));
        
        CreateRecipeParamsDTO paramsDTO = new CreateRecipeParamsDTO();
        paramsDTO.setName(DEFAULT_RECIPE_NAME);
        paramsDTO.setDescription(DEFAULT_RECIPE_DESCRIPTION);
        paramsDTO.setDuration(DEFAULT_RECIPE_DURATION);
        paramsDTO.setDiners(DEFAULT_RECIPE_DINERS);
        paramsDTO.setAuthorID(authorID);
        paramsDTO.setCategoryID(categoryID);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        paramsDTO.setIngredients(ingredientsParams);
        
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
    
    private String getI18NExceptionMessageWithParams(String propertyName, Locale locale, Object[] args, String value) {
        String exceptionMessage = messageSource.getMessage(
                value, null, value, locale
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
//        CommentID commentID = new CommentID(jwtData.getUserID(), recipe.getId());
//        Comment expectedResponse = commentRepository.findById(commentID).get();
//        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenCreateComment_thenUnauthorized_becausePermissionException() throws Exception {
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
              .andExpect(content().string(encodedResponseBodyContent)
        );
    }
    
    @Test
    void whenCreateComment_andRecipeDoesNotExist_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        CreateCommentParamsDTO paramsDTO = new CreateCommentParamsDTO(DEFAULT_COMMENT_TEXT, jwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/comments/" + NON_EXISTENT_UUID;
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale,
                new Object[] {Recipe.class.getSimpleName(), NON_EXISTENT_UUID},
                Recipe.class
        );
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenGetRecipeComments_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        socialService.commentRecipe(jwtData.getUserID(), recipe.getId(), DEFAULT_COMMENT_TEXT);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/comments/" + recipe.getId();
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        
        // Comprobar resultados
        // Block<Comment> expectedResponse = socialService.getRecipeComments(recipe.getId(), INITIAL_PAGE, PAGE_SIZE);
        // String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //      .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenGetRecipeComments_andRecipeDoesNotExist_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/comments/" + NON_EXISTENT_UUID;
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale,
                new Object[] {Recipe.class.getSimpleName(), NON_EXISTENT_UUID},
                Recipe.class
        );
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenBanCommentAsAdmin_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO adminDTO = loginAsAdmin();
        JwtData jwtData = jwtGenerator.extractInfo(adminDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        Comment comment = socialService.commentRecipe(jwtData.getUserID(), recipe.getId(), DEFAULT_COMMENT_TEXT);
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/comments/admin/ban/" + comment.getId();
        ResultActions action = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + adminDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        
        // Comprobar resultados
        CommentDTO expectedResponse = CommentConversor.toCommentDTO(comment);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenBanCommentAsAdmin_andCommentDoesNotExist_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO adminDTO = loginAsAdmin();
        JwtData jwtData = jwtGenerator.extractInfo(adminDTO.getServiceToken());
        Category category = registerCategory();
        registerRecipe(jwtData.getUserID(), category.getId());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/comments/admin/ban/" + NON_EXISTENT_UUID;
        ResultActions action = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + adminDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale,
                new Object[] {Comment.class.getSimpleName(), NON_EXISTENT_UUID},
                Comment.class
        );
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenBanCommentAsAdmin_andCurrentUserIsNotAdmin_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO adminDTO = loginAsAdmin();
        AuthenticatedUserDTO notAdminDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData notAdminJWTData = jwtGenerator.extractInfo(notAdminDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(notAdminJWTData.getUserID(), category.getId());
        Comment comment = socialService.commentRecipe(notAdminJWTData.getUserID(), recipe.getId(), DEFAULT_COMMENT_TEXT);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/comments/admin/ban/" + comment.getId();
        ResultActions action = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + adminDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", notAdminJWTData.getUserID())
                        .requestAttr("token", notAdminJWTData.toString())
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isForbidden())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenRateRecipe_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        int ratingValue = 10;
        RateRecipeParamsDTO paramsDTO = new RateRecipeParamsDTO(ratingValue, jwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/rate/" + recipe.getId();
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        
        // Comprobar resultados
        Recipe ratedRecipe = recipeService.getRecipeDetails(recipe.getId());
        RecipeDetailsDTO expectedResponse = RecipeConversor.toRecipeDetailsDTO(ratedRecipe);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenRateRecipeTwice_thenBadRequest_becauseRecipeAlreadyRatedException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        int ratingValue = 10;
        RateRecipeParamsDTO paramsDTO = new RateRecipeParamsDTO(ratingValue, jwtData.getUserID());
        
        // Ejecutar funcionalidades
        socialService.rateRecipe(jwtData.getUserID(), recipe.getId(), ratingValue);
        String endpointAddress = API_ENDPOINT + "/rate/" + recipe.getId();
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(encodedBodyContent)
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
                        .requestAttr("token", jwtData.toString())
        );
        String errorMessage = getI18NExceptionMessage(SocialController.RECIPE_ALREADY_RATED_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isBadRequest())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenRateRecipe_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        int ratingValue = 10;
        RateRecipeParamsDTO paramsDTO = new RateRecipeParamsDTO(ratingValue, jwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/rate/" + recipe.getId();
        String encodedBodyContent = this.jsonMapper.writeValueAsString(paramsDTO);
        ResultActions action = mockMvc.perform(
                post(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
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
              .andExpect(content().string(encodedResponseBodyContent)
        );
    }
    
    @Test
    void whenFollowUser_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/follow/" + requestorUserJwtData.getUserID() + "/" + targetUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", requestorUserJwtData.getUserID())
                        .requestAttr("token", requestorUserJwtData.toString())
        );
        
        // Comprobar resultados
        FollowID followID = new FollowID(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        Follow follow = followRepo.findById(followID).get();
        FollowDTO expectedResponse = FollowConversor.toFollowDTO(follow);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenFollowUserTwice_thenBadRequest_becauseUserAlreadyFollowedException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        
        // Ejecutar funcionalidades
        socialService.followUser(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        String endpointAddress = API_ENDPOINT + "/follow/" + requestorUserJwtData.getUserID() + "/" + targetUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", requestorUserJwtData.getUserID())
                        .requestAttr("token", requestorUserJwtData.toString())
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                SocialController.USER_ALREADY_FOLLOWED_EXCEPTION_KEY,
                locale,
                new Object[] {targetUserJwtData.getNickname()},
                targetUserJwtData.getNickname()
        );
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isBadRequest())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenFollowUser_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/follow/" + requestorUserJwtData.getUserID() + "/" + targetUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                put(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", UUID.randomUUID())
                        .requestAttr("token", requestorUserJwtData.toString())
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isForbidden())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent)
        );
    }
    
    @Test
    void whenUnfollowUser_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        socialService.followUser(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/unfollow/" + requestorUserJwtData.getUserID() + "/" + targetUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", requestorUserJwtData.getUserID())
                        .requestAttr("token", requestorUserJwtData.toString())
        );
        
        // Comprobar resultados
        action.andExpect(status().isNoContent());
    }
    
    @Test
    void whenUnfollowUser_andUserWasNotFollowed_thenBadRequest_becauseUserNotFollowedException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/unfollow/" + requestorUserJwtData.getUserID() + "/" + targetUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", requestorUserJwtData.getUserID())
                        .requestAttr("token", requestorUserJwtData.toString())
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                SocialController.USER_NOT_FOLLOWED_EXCEPTION_KEY,
                locale,
                new Object[] {targetUserJwtData.getNickname()},
                targetUserJwtData.getNickname()
        );
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenUnfollowUser_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        socialService.followUser(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/unfollow/" + requestorUserJwtData.getUserID() + "/" + targetUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", UUID.randomUUID())
                        .requestAttr("token", requestorUserJwtData.toString())
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isForbidden())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent)
        );
    }
    
    @Test
    void whenGetFollowers_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        socialService.followUser(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/followers/" + requestorUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", requestorUserJwtData.getUserID())
                        .requestAttr("token", requestorUserJwtData.toString())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        
        // Comprobar resultados
        Block<Follow> response = socialService.getFollowers(requestorUserJwtData.getUserID(), INITIAL_PAGE, PAGE_SIZE);
        BlockDTO<FollowDTO> expectedResponse = FollowConversor.toFollowBlockDTO(response);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenGetFollowings_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        socialService.followUser(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/followings/" + requestorUserJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", requestorUserJwtData.getUserID())
                        .requestAttr("token", requestorUserJwtData.toString())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        
        // Comprobar resultados
        Block<Follow> response = socialService.getFollowings(requestorUserJwtData.getUserID(), INITIAL_PAGE, PAGE_SIZE);
        BlockDTO<FollowDTO> expectedResponse = FollowConversor.toFollowBlockDTO(response);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenCheckUserIsFollowingTarget_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO requestorUserDTO = registerValidUser("requestor");
        JwtData requestorUserJwtData = jwtGenerator.extractInfo(requestorUserDTO.getServiceToken());
        AuthenticatedUserDTO targetUserDTO = registerValidUser("target");
        JwtData targetUserJwtData = jwtGenerator.extractInfo(targetUserDTO.getServiceToken());
        socialService.followUser(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/followings/" + requestorUserJwtData.getUserID() + "/check";
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + requestorUserDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", requestorUserJwtData.getUserID())
                        .requestAttr("token", requestorUserJwtData.toString())
                        .queryParam("targetID", targetUserJwtData.getUserID().toString())
        );
        
        // Comprobar resultados
        //boolean expectedResponse = socialService.doesFollowTarget(requestorUserJwtData.getUserID(), targetUserJwtData.getUserID());
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(true);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenGetUnreadNotifications_thenOK() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser("requestor");
        JwtData userJwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        socialService.createNotification(DEFAULT_NOTIFICATION_TITLE, DEFAULT_NOTIFICATION_MESSAGE, userJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/notifications/" + userJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", userJwtData.getUserID())
                        .requestAttr("token", userJwtData.toString())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        
        // Comprobar resultados
        Block<Notification> response = socialService.getUnreadNotifications(userJwtData.getUserID(), INITIAL_PAGE, PAGE_SIZE);
        BlockDTO<NotificationDTO> expectedResponse = NotificationConversor.toNotificationBlockDTO(response);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenGetUnreadNotifications_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser("requestor");
        JwtData userJwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        socialService.createNotification(DEFAULT_NOTIFICATION_TITLE, DEFAULT_NOTIFICATION_MESSAGE, userJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/notifications/" + userJwtData.getUserID();
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", UUID.randomUUID())
                        .requestAttr("token", userJwtData.toString())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isForbidden())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent)
              );
    }
    
    @Test
    void whenGetUnreadNotifications_andUserDoesNotExist_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser("requestor");
        JwtData userJwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        socialService.createNotification(DEFAULT_NOTIFICATION_TITLE, DEFAULT_NOTIFICATION_MESSAGE, userJwtData.getUserID());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/notifications/" + NON_EXISTENT_UUID;
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", NON_EXISTENT_UUID)
                        .requestAttr("token", userJwtData.toString())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale,
                new Object[] {User.class.getSimpleName(), NON_EXISTENT_UUID},
                User.class
        );
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
}

