package es.udc.fic.tfg.backendtfg.ingredients.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientRepository;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientTypeRepository;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.MeasureUnit;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private IngredientTypeRepository ingredientTypeRepo;
    
    @Autowired
    private IngredientRepository ingredientRepo;
    
    @Autowired
    private UserUtils userUtils;
    
    /* ******************** FUNCIONALIDADES INGREDIENTES ******************** */
    @Override
    public Ingredient createIngredient(String ingredientName, UUID ingredientTypeID, UUID userID)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        // Buscar el usuario. Si no existe lanza EntityNotFoundException
        User creator = userUtils.fetchUserByID(userID);
        
        // Comprobar si existe algún ingrediente con el mismo nombre
        if ( ingredientRepo.existsByNameLikeIgnoreCase(ingredientName))
            throw new EntityAlreadyExistsException(Ingredient.class.getSimpleName(), ingredientName);
        
        
        // Crear ingrediente
        Ingredient ingredient = new Ingredient();
        IngredientType ingredientType = fetchIngredientTypeByID(ingredientTypeID);
        ingredient.setIngredientType(ingredientType);
        ingredient.setName(ingredientName);
        ingredient.setCreator(creator);
        
        // Guardar datos y devolver instancia
        return ingredientRepo.save(ingredient);
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
        if ( ingredientTypeRepo.existsByNameIgnoreCase(ingredientTypeName))
            throw new EntityAlreadyExistsException(IngredientType.class.getSimpleName(), ingredientTypeName);
        
        // Crear tipo de ingrediente
        IngredientType type = new IngredientType();
        type.setName(ingredientTypeName);
        
        // Guardar datos y devolver instancia
        return ingredientTypeRepo.save(type);
    }
    
    @Override
    public List<IngredientType> getIngredientTypes() {
        List<IngredientType> results = new ArrayList<>();
        
        // Busca los tipos por orden alfabetico ascendente
        Sort ascNameSort = Sort.by(Direction.ASC, "name");
        Iterable<IngredientType> typesIterable = ingredientTypeRepo.findAll(ascNameSort);
        // Crea una lista de resultados
        typesIterable.forEach( (t) -> results.add(t));
        
        return results;
    }
    
    @Override
    public Block<Ingredient> findAllIngredients(int page, int pageSize) {
        // Buscar los ingredientes
        Slice<Ingredient> ingredientSlice = ingredientRepo.findByOrderByNameAsc(PageRequest.of(page, pageSize));
        
        // Devuelve los resultados
        return new Block<>(ingredientSlice.getContent(), ingredientSlice.hasNext(), ingredientSlice.getNumberOfElements());
    }
    
    @Override
    public Block<Ingredient> findIngredientsByName(String name, int page, int pageSize) {
        // Busca los ingredientes por nombre en orden alfabético ascendente
        Slice<Ingredient> ingredientSlice = ingredientRepo.findByNameContainsIgnoreCaseOrderByNameAsc(name, PageRequest.of(page, pageSize));
        
        // Devuelve resultados
        return new Block<>(ingredientSlice.getContent(), ingredientSlice.hasNext(), ingredientSlice.getNumberOfElements());
    }
    
    @Override
    public Block<Ingredient> findIngredientsByType(UUID ingredientTypeID, int page, int pageSize) {
        // Busca los ingredientes por tipo en orden alfabético ascendente
        Slice<Ingredient> ingredientSlice = ingredientRepo.findByIngredientType_IdOrderByNameAsc(ingredientTypeID, PageRequest.of(page, pageSize));
    
        // Devuelve resultados
        return new Block<>(ingredientSlice.getContent(), ingredientSlice.hasNext(), ingredientSlice.getNumberOfElements());
    }
    
    @Override
    public Block<Ingredient> findIngredientsByNameAndType(String name, UUID ingredientTypeID, int page, int pageSize) {
        // Busca los ingredientes por nombre y tipo en orden alfabético ascendente
        Slice<Ingredient> ingredientSlice = ingredientRepo
                .findByNameContainsIgnoreCaseAndIngredientType_IdOrderByNameAsc(name, ingredientTypeID, PageRequest.of(page, pageSize));
    
        // Devuelve resultados
        return new Block<>(ingredientSlice.getContent(), ingredientSlice.hasNext(), ingredientSlice.getNumberOfElements());
    }
    
    @Override
    public List<MeasureUnit> getAllMeasureUnits( ) {
        return Arrays.stream(MeasureUnit.values())
                     .sorted()
                     .collect(Collectors.toList());
    }
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    private IngredientType fetchIngredientTypeByID(UUID ingredientTypeID) throws EntityNotFoundException {
        Optional<IngredientType> optionalIngredientType = ingredientTypeRepo.findById(ingredientTypeID);
        
        if ( optionalIngredientType.isEmpty())
            throw new EntityNotFoundException(IngredientType.class.getSimpleName(), ingredientTypeID);
        
        return optionalIngredientType.get();
    }
}
