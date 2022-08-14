package es.udc.fic.tfg.backendtfg.ingredients.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.*;

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
    IngredientType createIngredientTypeAsAdmin(String ingredientTypeName, UUID adminID)
            throws EntityAlreadyExistsException, EntityNotFoundException, PermissionException;
    
    /**
     * Recupera todos los tipos de ingredientes.
     * @return Lista de tipos de ingredientes
     */
    List<IngredientType> getIngredientTypes();
    
    /**
     * Recupera todos los ingredientes paginados
     * @param page Número de página a cargar
     * @param pageSize Tamaño de la página
     * @return Bloque con los ingredientes
     */
    Block<Ingredient> findAllIngredients(int page, int pageSize);
    
    /**
     * Recupera todos los ingredientes que contengan el nombre recibido
     * @param name Nombre del ingrediente a buscar
     * @param page Número de página a cargar
     * @param pageSize Tamaño de la página
     * @return Bloque de ingredientes que contengan el nombre
     */
    Block<Ingredient> findIngredientsByName(String name, int page, int pageSize);
    
    /**
     * Recupera todos los ingredientes que pertenezcan al tipo recibido
     * @param ingredientTypeID Tipo de ingrediente a filtrar
     * @param page Número de página a cargar
     * @param pageSize Tamaño de la página
     * @return Bloque de ingredientes que pertenezcan a la categoría
     */
    Block<Ingredient> findIngredientsByType(UUID ingredientTypeID, int page, int pageSize);
    
    /**
     * Recupera todos los ingredientes que contengan el nombre recibido y pertenezcan a un tipo dado
     * @param ingredientTypeID Tipo de ingrediente a filtrar
     * @param name Nombre del ingrediente a buscar
     * @param page Número de página a cargar
     * @param pageSize Tamaño de la página
     * @return Bloque de ingredientes que coincidan con la búsqueda
     */
    Block<Ingredient> findIngredientsByNameAndType(String name, UUID ingredientTypeID, int page, int pageSize);
    
    /**
     * Recupera todas las unidades de medidas de los ingredientes.
     * @return Lista de unidades de medida
     */
    List<MeasureUnit> getAllMeasureUnits();
}
