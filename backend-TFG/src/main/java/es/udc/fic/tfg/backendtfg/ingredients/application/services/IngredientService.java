package es.udc.fic.tfg.backendtfg.ingredients.application.services;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityAlreadyExistsException;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;

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
}
