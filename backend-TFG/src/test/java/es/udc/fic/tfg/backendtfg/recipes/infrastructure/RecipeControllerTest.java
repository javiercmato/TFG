package es.udc.fic.tfg.backendtfg.recipes.infrastructure;

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
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.*;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.controllers.RecipeController;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.CategoryConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.RecipeConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
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
public class RecipeControllerTest {
    private static final String API_ENDPOINT = "/api/recipes";
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final int INITIAL_PAGE = 0;
    private final int PAGE_SIZE = 5;
    
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
    
    /** Registra un tipo de ingrediente válido. */
    private IngredientType registerIngredientType() {
        IngredientType type = new IngredientType();
        type.setName(DEFAULT_INGREDIENTTYPE_NAME);
        
        return ingredientTypeRepository.save(type);
    }
    
    /** Registra un ingrediente válido */
    private Ingredient registerIngredient(String name, UUID ingredientTypeID, UUID authorID) throws Exception {
        return ingredientService.createIngredient(name, ingredientTypeID, authorID);
    }
    
    /** Añade el ingrediente recibido a la receta */
    private RecipeIngredient addIngredientToRecipe(Ingredient ingredient, Recipe recipe) {
        // Crear ingrediente de receta
        RecipeIngredientID id = new RecipeIngredientID(recipe.getId(), ingredient.getId());
        RecipeIngredient recipeIngredient = new RecipeIngredient(id, "1", MeasureUnit.UNIDAD, recipe, ingredient);
        
        recipe.addIngredient(recipeIngredient);
        
        return recipeIngredientRepo.save(recipeIngredient);
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
    
    @Test
    void whenCreateRecipe_thenRecipeIsCreated() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        List<CreateRecipeStepParamsDTO> stepsParams = new ArrayList<>();
        stepsParams.add(new CreateRecipeStepParamsDTO(1, DEFAULT_RECIPESTEP_TEXT));
        IngredientType ingredientType = registerIngredientType();
        Ingredient ingredient = ingredientService.createIngredient(DEFAULT_INGREDIENT_NAME, ingredientType.getId(), jwtData.getUserID());
        List<CreateRecipeIngredientParamsDTO> ingredientsParams = new ArrayList<>();
        ingredientsParams.add(new CreateRecipeIngredientParamsDTO(ingredient.getId(), "1", MeasureUnit.UNIDAD.toString()));
        List<CreateRecipePictureParamsDTO> picturesParams = new ArrayList<>();
        picturesParams.add(new CreateRecipePictureParamsDTO(1,
            Base64.getEncoder().encodeToString(loadImageFromResourceName(DEFAULT_RECIPE_IMAGE_1, PNG_EXTENSION))
        ));
        Category category = registerCategory();
        CreateRecipeParamsDTO paramsDTO = generateCreateRecipeParamsDTO(jwtData.getUserID(), category.getId());
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/";
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
        //Recipe expectedResponse = recipeRepo.findAll().iterator().next();
        //byte[] encodedResponseBodyContent = this.jsonMapper.writeValueAsBytes(expectedResponse);
        action.andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //      .andExpect(content().bytes(encodedResponseBodyContent));
    }
    
    @Test
    void whenCreateRecipeAsOtherUser_thenUnauthorized_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        List<CreateRecipeStepParamsDTO> stepsParams = new ArrayList<>();
        stepsParams.add(new CreateRecipeStepParamsDTO(1, DEFAULT_RECIPESTEP_TEXT));
        IngredientType ingredientType = registerIngredientType();
        Ingredient ingredient = ingredientService.createIngredient(DEFAULT_INGREDIENT_NAME, ingredientType.getId(), jwtData.getUserID());
        List<CreateRecipeIngredientParamsDTO> ingredientsParams = new ArrayList<>();
        ingredientsParams.add(new CreateRecipeIngredientParamsDTO(ingredient.getId(), "1", MeasureUnit.UNIDAD.toString()));
        List<CreateRecipePictureParamsDTO> picturesParams = new ArrayList<>();
        Category category = registerCategory();
        CreateRecipeParamsDTO paramsDTO = generateCreateRecipeParamsDTO(jwtData.getUserID(), category.getId());
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/";
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
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenCreateRecipeWithoutSteps_thenBadRequest_becauseEmptyRecipeStepsListException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        List<CreateRecipeStepParamsDTO> stepsParams = new ArrayList<>();
        // stepsParams.add(new CreateRecipeStepParamsDTO(1, DEFAULT_RECIPESTEP_TEXT));
        IngredientType ingredientType = registerIngredientType();
        Ingredient ingredient = ingredientService.createIngredient(DEFAULT_INGREDIENT_NAME, ingredientType.getId(), jwtData.getUserID());
        List<CreateRecipeIngredientParamsDTO> ingredientsParams = new ArrayList<>();
        ingredientsParams.add(new CreateRecipeIngredientParamsDTO(ingredient.getId(), "1", MeasureUnit.UNIDAD.toString()));
        List<CreateRecipePictureParamsDTO> picturesParams = new ArrayList<>();
        Category category = registerCategory();
        CreateRecipeParamsDTO paramsDTO = generateCreateRecipeParamsDTO(jwtData.getUserID(), category.getId());
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/";
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
        String errorMessage = getI18NExceptionMessage(RecipeController.EMPTY_RECIPE_STEPS_EXCEPTION_KEY, locale);
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isBadRequest())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenCreateRecipe_andCategoryDoesNotExist_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        List<CreateRecipeStepParamsDTO> stepsParams = new ArrayList<>();
        // stepsParams.add(new CreateRecipeStepParamsDTO(1, DEFAULT_RECIPESTEP_TEXT));
        IngredientType ingredientType = registerIngredientType();
        Ingredient ingredient = ingredientService.createIngredient(DEFAULT_INGREDIENT_NAME, ingredientType.getId(), jwtData.getUserID());
        List<CreateRecipeIngredientParamsDTO> ingredientsParams = new ArrayList<>();
        ingredientsParams.add(new CreateRecipeIngredientParamsDTO(ingredient.getId(), "1", MeasureUnit.UNIDAD.toString()));
        List<CreateRecipePictureParamsDTO> picturesParams = new ArrayList<>();
        CreateRecipeParamsDTO paramsDTO = generateCreateRecipeParamsDTO(jwtData.getUserID(), NON_EXISTENT_UUID);
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/";
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
                new Object[] {Category.class.getSimpleName(), NON_EXISTENT_UUID},
                User.class
        );
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenCreateRecipe_andIngredientDoesNotExist_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        List<CreateRecipeStepParamsDTO> stepsParams = new ArrayList<>();
        stepsParams.add(new CreateRecipeStepParamsDTO(1, DEFAULT_RECIPESTEP_TEXT));
        List<CreateRecipeIngredientParamsDTO> ingredientsParams = new ArrayList<>();
        ingredientsParams.add(new CreateRecipeIngredientParamsDTO(NON_EXISTENT_UUID, "1", MeasureUnit.UNIDAD.toString()));
        List<CreateRecipePictureParamsDTO> picturesParams = new ArrayList<>();
        Category category = registerCategory();
        CreateRecipeParamsDTO paramsDTO = generateCreateRecipeParamsDTO(jwtData.getUserID(), category.getId());
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        paramsDTO.setIngredients(ingredientsParams);
        paramsDTO.setPictures(picturesParams);
        paramsDTO.setSteps(stepsParams);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/";
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
                new Object[] {Ingredient.class.getSimpleName(), NON_EXISTENT_UUID},
                User.class
        );
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenGetRecipeDetails_thenRecipeSummaryDTOIsRetrieved() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        Recipe createdRecipe = registerRecipe(jwtData.getUserID(), category.getId());
    
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + createdRecipe.getId().toString();
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
        );
    
        // Comprobar resultados
        //Recipe foundRecipe = recipeService.getRecipeDetails(createdRecipe.getId());
        Recipe foundRecipe = recipeRepo.findById(createdRecipe.getId()).get();
        RecipeDetailsDTO expectedResponse = RecipeConversor.toRecipeDetailsDTO(foundRecipe);
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenGetRecipeDetails_andRecipeDoesNotExist_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + NON_EXISTENT_UUID;
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                    .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
        );
        String errorMessage = getI18NExceptionMessageWithParams(
                CommonControllerAdvice.ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale,
                new Object[] {Recipe.class.getSimpleName(), NON_EXISTENT_UUID},
                User.class
        );
        
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isNotFound())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenFindRecipes_withoutCriteria_thenRecipesBlockIsRetrieved() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        IngredientType type = registerIngredientType();
        Ingredient ingredient = registerIngredient(DEFAULT_INGREDIENT_NAME, type.getId(), jwtData.getUserID());
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        addIngredientToRecipe(ingredient, recipe);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/find";
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
        );
        
        // Comprobar resultados
        Block<Recipe> recipes = recipeService.findRecipesByCriteria(null, null, null, INITIAL_PAGE, PAGE_SIZE);
        List<RecipeSummaryDTO> recipeSummaryDTOList = RecipeConversor.toRecipeSummaryListDTO(recipes.getItems());
        BlockDTO<RecipeSummaryDTO> expectedResponse = new BlockDTO<>(recipeSummaryDTOList, recipes.hasMoreItems(), recipes.getItemsCount());
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenFindRecipes_withAllCriterias_thenRecipesBlockIsRetrieved() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        IngredientType type = registerIngredientType();
        Ingredient ingredient = registerIngredient(DEFAULT_INGREDIENT_NAME, type.getId(), jwtData.getUserID());
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        addIngredientToRecipe(ingredient, recipe);
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/find";
        ResultActions action = mockMvc.perform(
                get(endpointAddress)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        .queryParam("page", String.valueOf(INITIAL_PAGE))
                        .queryParam("pageSize", String.valueOf(PAGE_SIZE))
                        .queryParam("name", recipe.getName())
                        .queryParam("categoryID", category.getId().toString())
                        .queryParam("ingredientIdList", ingredient.getId().toString())
        );
        
        // Comprobar resultados
        Block<Recipe> recipes = recipeService.findRecipesByCriteria(
                recipe.getName(), recipe.getCategory().getId(), List.of(ingredient.getId()), INITIAL_PAGE, PAGE_SIZE);
        List<RecipeSummaryDTO> recipeSummaryDTOList = RecipeConversor.toRecipeSummaryListDTO(recipes.getItems());
        BlockDTO<RecipeSummaryDTO> expectedResponse = new BlockDTO<>(recipeSummaryDTOList, recipes.hasMoreItems(), recipes.getItemsCount());
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(expectedResponse);
        action.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenDeleteRecipe_thenNoContent() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        IngredientType type = registerIngredientType();
        Ingredient ingredient = registerIngredient(DEFAULT_INGREDIENT_NAME, type.getId(), jwtData.getUserID());
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        addIngredientToRecipe(ingredient, recipe);
        String recipeID = recipe.getId().toString();
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + recipeID;
        ResultActions action = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
        );
        
        // Comprobar resultados
        action.andExpect(status().isNoContent());
    }
    
    @Test
    void whenDeleteRecipe_andUserIsNotAuthor_thenNotAllowed_becausePermissionException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        Recipe recipe = registerRecipe(jwtData.getUserID(), category.getId());
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + recipe.getId();
        ResultActions action = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", UUID.randomUUID())
        );
        String errorMessage = getI18NExceptionMessage(CommonControllerAdvice.PERMISION_EXCEPTION_KEY, locale);
    
        // Comprobar resultados
        String encodedResponseBodyContent = this.jsonMapper.writeValueAsString(new ErrorsDTO(errorMessage));
        action.andExpect(status().isForbidden())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(content().string(encodedResponseBodyContent));
    }
    
    @Test
    void whenDeleteNonExistentRecipe_thenNotFound_becauseEntityNotFoundException() throws Exception {
        // Crear datos de prueba
        AuthenticatedUserDTO userDTO = registerValidUser(DEFAULT_NICKNAME);
        JwtData jwtData = jwtGenerator.extractInfo(userDTO.getServiceToken());
        Category category = registerCategory();
        
        // Ejecutar funcionalidades
        String endpointAddress = API_ENDPOINT + "/" + NON_EXISTENT_UUID;
        ResultActions action = mockMvc.perform(
                delete(endpointAddress)
                        .header(HttpHeaders.AUTHORIZATION, AUTH_TOKEN_PREFIX + userDTO.getServiceToken())
                        .header(HttpHeaders.ACCEPT_LANGUAGE, locale.getLanguage())
                        // Valores anotados como @RequestAttribute
                        .requestAttr("userID", jwtData.getUserID())
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
}
