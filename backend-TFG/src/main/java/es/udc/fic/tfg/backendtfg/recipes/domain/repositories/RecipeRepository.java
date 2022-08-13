package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface RecipeRepository extends PagingAndSortingRepository<Recipe, UUID> {

}
