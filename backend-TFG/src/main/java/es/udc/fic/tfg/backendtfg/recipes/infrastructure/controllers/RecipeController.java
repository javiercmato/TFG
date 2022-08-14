package es.udc.fic.tfg.backendtfg.recipes.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.recipes.application.RecipeService;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.exceptions.EmptyRecipeStepsListException;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.CategoryConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.RecipeConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private RecipeService recipeService;
    
    
    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    
    
    
    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    
    
    
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
            @RequestAttribute("userID") UUID userID) throws EmptyRecipeStepsListException, EntityNotFoundException {
        // Llamada al serivicio
        Recipe recipe = recipeService.createRecipe(params);
        
        // Generar respuesta
        return RecipeConversor.toRecipeDTO(recipe);
    }
    
    
    
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    
    
    
}
