package es.udc.fic.tfg.backendtfg.recipes.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.CategoryRepository;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private CategoryRepository categoryRepo;
    
    @Autowired
    private UserUtils userUtils;
    
    
    /* ******************** FUNCIONALIDADES INGREDIENTES ******************** */
    
    @Override
    public Category createCategoryAsAdmin(String categoryName, UUID adminID)
            throws EntityAlreadyExistsException, EntityNotFoundException, PermissionException {
        // Commprobar si existe el administrador
        try {
            userUtils.fetchAdministrator(adminID);
        } catch ( EntityNotFoundException ex) {
            throw new PermissionException();
        }
        
        // Comprobar si ya existe la categoría
        if ( categoryRepo.existsByNameIgnoreCase(categoryName) )
            throw new EntityAlreadyExistsException(Category.class.getSimpleName(), categoryName);
        
        // Crear categoría
        Category category = new Category();
        category.setName(categoryName);
        
        // Guardar datos y devolver instancia
        return categoryRepo.save(category);
    }
    
    
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    
    
}
