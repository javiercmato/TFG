package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.RecipeStep;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.RecipeStepID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipeStepRepository extends PagingAndSortingRepository<RecipeStep, RecipeStepID> {
    
}
