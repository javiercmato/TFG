package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public interface CustomizedRecipeRepository {
    
    /**
     * Busca recetas paginadas por nombre, categoría o lista de ingredientes
     */
    Slice<Recipe> findByCriteria(String name, UUID categoryID, List<UUID> ingredientsID, int page, int pageSize);
    
    /**
     * Busca recetas paginadas creadas por el usuario recibido
     */
    Slice<Recipe> findByAuthor(UUID userID, int page, int pageSize);
    
}
