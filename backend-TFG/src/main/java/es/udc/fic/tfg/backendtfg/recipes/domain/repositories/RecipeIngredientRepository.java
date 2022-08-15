package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.RecipeIngredient;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.RecipeIngredientID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipeIngredientRepository extends PagingAndSortingRepository<RecipeIngredient, RecipeIngredientID> {
    
}
