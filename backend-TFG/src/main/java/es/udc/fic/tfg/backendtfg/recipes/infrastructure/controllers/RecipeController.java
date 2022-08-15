package es.udc.fic.tfg.backendtfg.recipes.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.recipes.application.RecipeService;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.exceptions.EmptyRecipeStepsListException;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.CategoryConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.RecipeConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            throws EmptyRecipeStepsListException, EntityNotFoundException, PermissionException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!userControllerUtils.doUsersMatch(userID, params.getAuthorID()))
            throw new PermissionException();
        
        // Llamada al serivicio
        Recipe recipe = recipeService.createRecipe(params);
        
        // Generar respuesta
        return RecipeConversor.toRecipeDTO(recipe);
    }
    
    
    
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    
    
    
}
