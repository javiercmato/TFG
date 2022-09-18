package es.udc.fic.tfg.backendtfg.recipes.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.recipes.application.RecipeService;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.exceptions.EmptyRecipeIngredientsListException;
import es.udc.fic.tfg.backendtfg.recipes.domain.exceptions.EmptyRecipeStepsListException;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.CategoryConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.RecipeConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private UserControllerUtils userControllerUtils;
    
    
    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    // Referencias a los errores en los ficheros de i18n
    public static final String EMPTY_RECIPE_STEPS_EXCEPTION_KEY         = "recipes.domain.exceptions.EmptyRecipeStepsException";
    public static final String EMPTY_RECIPE_INGREDIENTS_EXCEPTION_KEY         = "recipes.domain.exceptions.EmptyRecipeIngredientsException";
    
    
    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(EmptyRecipeStepsListException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)         // 400
    @ResponseBody
    public ErrorsDTO handleEmptyRecipeStepsListException(EmptyRecipeStepsListException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                EMPTY_RECIPE_STEPS_EXCEPTION_KEY, null, EMPTY_RECIPE_STEPS_EXCEPTION_KEY, locale
        );
    
        return new ErrorsDTO(errorMessage);
    }
    
    @ExceptionHandler(EmptyRecipeIngredientsListException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)         // 400
    @ResponseBody
    public ErrorsDTO handleEmptyRecipeIngredientsListExceptionn(EmptyRecipeIngredientsListException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                EMPTY_RECIPE_INGREDIENTS_EXCEPTION_KEY, null, EMPTY_RECIPE_INGREDIENTS_EXCEPTION_KEY, locale
        );
        
        return new ErrorsDTO(errorMessage);
    }
    
    
    
    /* ******************** ENDPOINTS ******************** */
    @PostMapping(path = "/categories",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createCategoryAsAdmin(@Validated @RequestBody CreateCategoryParamsDTO params,
                                            @RequestAttribute("userID") UUID adminID)
            throws PermissionException, EntityAlreadyExistsException {
        // Llamada al servicio
        Category category = recipeService.createCategoryAsAdmin(params.getName(), adminID);
        
        // Generar respuesta
        return CategoryConversor.toCategoryDTO(category);
    }
    
    @GetMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryDTO> getAllCategories() {
        // Llamada al servicio
        List<Category> categories = recipeService.getAllCategories();
        
        // Generar respuesta
        return CategoryConversor.toCategoryListDTO(categories);
    }
    
    @PostMapping(path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDTO createRecipe(@Validated @RequestBody CreateRecipeParamsDTO params,
                                  @RequestAttribute("userID") UUID userID)
            throws EmptyRecipeStepsListException, EntityNotFoundException, PermissionException, EmptyRecipeIngredientsListException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!userControllerUtils.doUsersMatch(userID, params.getAuthorID()))
            throw new PermissionException();
        
        // Llamada al serivicio
        Recipe recipe = recipeService.createRecipe(params);
        
        // Generar respuesta
        return RecipeConversor.toRecipeDTO(recipe);
    }
    
    @GetMapping(path = "/{recipeID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeDetailsDTO getRecipeDetails(@PathVariable("recipeID") UUID recipeID) throws EntityNotFoundException {
        // Llamada al servicio
        Recipe recipe = recipeService.getRecipeDetails(recipeID);
        
        // Generar respuesta
        return RecipeConversor.toRecipeDetailsDTO(recipe);
    }
    
    @GetMapping(
            path = "find",
            //params = {"name", "categoryID", "ingredients"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BlockDTO<RecipeSummaryDTO> findRecipes(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "categoryID", required = false) UUID categoryID,
                                                  @RequestParam(value = "ingredientIdList", required = false) List<UUID> ingredientIdList,
                                                  @RequestParam("page") int page,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // Llamada al servicio
        Block<Recipe> recipesBlock = recipeService.findRecipesByCriteria(name, categoryID, ingredientIdList, page, pageSize);
        
        // Generar respuesta
        List<RecipeSummaryDTO> recipeSummaryDTOList = RecipeConversor.toRecipeSummaryListDTO(recipesBlock.getItems());
        
        return createBlock(recipeSummaryDTOList, recipesBlock.hasMoreItems(), recipesBlock.getItemsCount());
    }
    
    @DeleteMapping(path = "/{recipeID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteRecipe(@RequestAttribute("userID") UUID userID,
                                      @PathVariable("recipeID") UUID pathRecipeID)
        throws EntityNotFoundException, PermissionException {
        // Llamada al servicio
        recipeService.deleteRecipe(pathRecipeID, userID);
        
        // Generar respuesta
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping(path = "/admin/ban/{recipeID}")
    public boolean banRecipeAsAdmin(@RequestAttribute("userID") UUID adminID,
                                    @PathVariable("recipeID") UUID targetRecipeID)
            throws EntityNotFoundException, PermissionException {
        // Llamada al servicio
        return recipeService.banRecipeAsAdmin(adminID, targetRecipeID);
    }
    
    
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    private <T> BlockDTO<T> createBlock(List<T> items, boolean hasMoreItems, int itemsCount) {
        BlockDTO<T> block = new BlockDTO<>();
        block.setItems(items);
        block.setItemsCount(itemsCount);
        block.setHasMoreItems(hasMoreItems);
    
        return block;
    }
    
    
}
