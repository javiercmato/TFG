package es.udc.fic.tfg.backendtfg.recipes.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.exceptions.EmptyRecipeStepsListException;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.CreateRecipeParamsDTO;

import java.util.List;
import java.util.UUID;

public interface RecipeService {
    
    /**
     * Crea una nueva categoría de recetas
     * Caso de uso restringido al administrador.
     * @param categoryName Nombre de la nueva categoría
     * @param adminID ID del administrador
     * @return Categoría creada
     * @throws EntityAlreadyExistsException Categoría ya existe
     * @throws PermissionException Usuario no es administrador
     */
    Category createCategoryAsAdmin(String categoryName, UUID adminID)
            throws EntityAlreadyExistsException, PermissionException;
    
    /**
     * Recupera todas las categorías de recetas.
     * @return Lista de categorías
     */
    List<Category> getAllCategories();
    
    /**
     * Crea una receta a partir de los datos recibidos.
     * @param recipeParams Parámetros para crer una receta
     * @return Receta creada
     * @throws EmptyRecipeStepsListException Se intenta crear una receta sin pasos
     * @throws EntityNotFoundException No se encuentra al usuario o algún ingrediente
     */
    Recipe createRecipe(CreateRecipeParamsDTO recipeParams) throws EmptyRecipeStepsListException, EntityNotFoundException;
    
    /**
     * Recupera la información de una receta.
     * @param recipeID ID de la receta a buscar
     * @return Receta encontrada
     * @throws EntityNotFoundException No se encuentra la receta
     */
    Recipe getRecipeDetails(UUID recipeID) throws EntityNotFoundException;
}
