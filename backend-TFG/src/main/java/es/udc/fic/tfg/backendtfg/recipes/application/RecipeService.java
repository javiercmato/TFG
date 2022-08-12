package es.udc.fic.tfg.backendtfg.recipes.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;

import java.util.UUID;

public interface RecipeService {
    
    /**
     * Crea una nueva categoría de recetas
     * Caso de uso restringido al administrador.
     * @param categoryName Nombre de la nueva categoría
     * @param adminID ID del administrador
     * @return Categoría creada
     * @throws EntityAlreadyExistsException Categoría ya existe
     * @throws EntityNotFoundException Usuario no existe
     * @throws PermissionException Usuario no es administrador
     */
    Category createCategoryAsAdmin(String categoryName, UUID adminID)
            throws EntityAlreadyExistsException, EntityNotFoundException, PermissionException;
    
    
    
}
