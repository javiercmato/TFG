package es.udc.fic.tfg.backendtfg.ingredients.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;

import java.util.List;
import java.util.UUID;

public interface IngredientService {
    
    /**
     * Crea un nuevo ingrediente.
     * @param ingredientName Nombre del nuevo ingrediente
     * @param userID ID del usuario que crea el ingrediente
     * @return Ingrediente creado
     * @throws EntityAlreadyExistsException Ingrediente ya existe
     * @throws EntityNotFoundException Usuario no existe
     */
    Ingredient createIngredient(String ingredientName, UUID ingredientTypeID, UUID userID)
            throws EntityAlreadyExistsException, EntityNotFoundException;
    
    /**
     * Crea un nuevo tipo de ingrediente.
     * Caso de uso restringido al administrador.
     * @param ingredientTypeName Nombre del nuevo tipo de ingrediente
     * @param adminID ID del administrador
     * @return Tipo de ingrediente creado
     * @throws EntityAlreadyExistsException Tipo de ingrediente ya existe
     * @throws EntityNotFoundException Usuario no existe
     * @throws PermissionException Usuario no es administrador
     */
    IngredientType createIngredientTypeAsAdmin(String ingredientTypeName, UUID adminID) throws EntityAlreadyExistsException,
                                                                                               EntityNotFoundException,
                                                                                               PermissionException;
    
    /**
     * Obtiene todos los tipos de ingredientes.
     * @return Lista de tipos de ingredientes
     */
    List<IngredientType> getIngredientTypes();
}
