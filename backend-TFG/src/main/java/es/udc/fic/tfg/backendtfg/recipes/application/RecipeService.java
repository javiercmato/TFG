package es.udc.fic.tfg.backendtfg.recipes.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
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
    
    /**
     * Recupera todas las recetas que contengan el nombre, pertenezcan a la categoría o contengan los ingredientes dados.
     * Los resultados están ordenados por fecha de creación descendiente (las más recientes primero).
     * Si no se recibe ningún criterio, se devuelven todas las recetas.
     * @param name Nombre de la receta a buscar (opcional)
     * @param categoryId ID de la categoría de la receta (opcional)
     * @param ingredientIDsList Lista de ID de los ingredientes que debe contener la receta (opcional)
     * @param page Número de página a cargar
     * @param pageSize Tamaño de página
     * @return Bloque de recetas que coincidan con la búsqueda, o todas las recetas si no hay ningún criterio.
     */
    Block<Recipe> findRecipesByCriteria(String name, UUID categoryId, List<UUID> ingredientIDsList, int page, int pageSize);
    
    /**
     * Elimina una receta.
     * @param recipeID ID de la receta a eliminar
     * @param recipeID ID de del usuario que solicita el borrado
     * @throws EntityNotFoundException No se encuentra la receta
     * @throws PermissionException Usuario no es propietario de la receta
     */
    void deleteRecipe(UUID recipeID, UUID userID) throws EntityNotFoundException, PermissionException;
    
    /**
     * Banea una receta del sistema.
     * Si la receta estaba baneada previamente, la desbanea.
     * Caso de uso restringido al administrador.
     * @param adminID ID del administrador
     * @param recipeID ID de la receta a banear
     * @return True si la receta está baneada: false si no está baneada.
     * @throws EntityNotFoundException No se encuentra la receta
     * @throws PermissionException El usuario no es administrador
     */
    boolean banRecipeAsAdmin(UUID adminID, UUID recipeID) throws EntityNotFoundException, PermissionException;
    
    /**
     * Añade la receta recibida a la lista privada recibida.
     * @param listID ID de la lista privada
     * @param recipeID ID de la receta a añadir
     * @throws EntityNotFoundException No se encuentra la lista o la receta
     */
    void addRecipeToPrivateList(UUID listID, UUID recipeID) throws EntityNotFoundException;
}
