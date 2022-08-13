package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.PrivateListRecipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.PrivateListRecipeID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PrivateListRecipeRepository extends PagingAndSortingRepository<PrivateListRecipe, PrivateListRecipeID> {
    
}
