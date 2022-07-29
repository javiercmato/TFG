package es.udc.fic.tfg.backendtfg.ingredients.application.services;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientRepository;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientTypeRepository;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private IngredientTypeRepository ingredientTypeRepository;
    
    @Autowired
    private IngredientRepository ingredientRepository;
    
    @Autowired
    private UserUtils userUtils;
    
    /* ******************** FUNCIONALIDADES INGREDIENTES ******************** */
    @Override
    public Ingredient createIngredient(String ingredientName, UUID ingredientTypeID, UUID userID)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        // Buscar el usuario. Si no existe lanza EntityNotFoundException
        User creator = userUtils.fetchUserByID(userID);
        
        // Comprobar si existe algún ingrediente con el mismo nombre
        if (ingredientRepository.existsByNameLikeIgnoreCase(ingredientName))
            throw new EntityAlreadyExistsException(Ingredient.class.getSimpleName(), ingredientName);
        
        
        // Crear ingrediente
        Ingredient ingredient = new Ingredient();
        IngredientType ingredientType = fetchIngredientTypeByID(ingredientTypeID);
        ingredient.setIngredientType(ingredientType);
        ingredient.setName(ingredientName);
        ingredient.setCreator(creator);
        
        // Guardar datos y devolver instancia
        return ingredientRepository.save(ingredient);
    }
    
    @Override
    public IngredientType createIngredientTypeAsAdmin(String ingredientTypeName, UUID adminID)
            throws EntityAlreadyExistsException, PermissionException {
        // Commprobar si existe el administrador
        try {
            userUtils.fetchAdministrator(adminID);
        } catch ( EntityNotFoundException ex) {
            throw new PermissionException();
        }
        
        // Comprobar si ya existe el tipo
        if (ingredientTypeRepository.existsByNameIgnoreCase(ingredientTypeName))
            throw new EntityAlreadyExistsException(IngredientType.class.getSimpleName(), ingredientTypeName);
        
        // Crear tipo de ingrediente
        IngredientType type = new IngredientType();
        type.setName(ingredientTypeName);
        
        // Guardar datos y devolver instancia
        return ingredientTypeRepository.save(type);
    }
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    private IngredientType fetchIngredientTypeByID(UUID ingredientTypeID) throws EntityNotFoundException {
        Optional<IngredientType> optionalIngredientType = ingredientTypeRepository.findById(ingredientTypeID);
        
        if ( optionalIngredientType.isEmpty())
            throw new EntityNotFoundException(IngredientType.class.getSimpleName(), ingredientTypeID);
        
        return optionalIngredientType.get();
    }
}
