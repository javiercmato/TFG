package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends PagingAndSortingRepository<Recipe, UUID> {

    @Query(
            "SELECT DISTINCT r\n" +
                    "FROM Recipe r INNER JOIN FETCH r.author author\n" +
                    "INNER JOIN FETCH r.ingredients\n" +
                    "INNER JOIN FETCH r.steps\n" +
                    "INNER JOIN FETCH r.pictures\n" +
                    "WHERE r.id = ?1"
    )
    Optional<Recipe> retrieveRecipeDetails(UUID recipeID);
}
