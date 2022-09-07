package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends PagingAndSortingRepository<Recipe, UUID>, CustomizedRecipeRepository {

    @Query(
            "SELECT r \n" +
                "FROM Recipe r LEFT JOIN FETCH r.author \n" +
                "LEFT JOIN FETCH r.ingredients \n" +
                "LEFT JOIN FETCH r.steps \n" +
                "LEFT JOIN FETCH r.pictures \n" +
                "WHERE r.id = ?1"
    )
    Optional<Recipe> retrieveRecipeDetails(UUID recipeID);
    
    /** Devuelve las recetas que pertenecen a la lista privada recibida, ordenadas por fecha de inserción descendente */
    Slice<Recipe> findByPrivateListRecipes_Id_PrivateListIDOrderByPrivateListRecipes_InsertionDateDesc(
            UUID privateListID, Pageable pageable);
    
}
