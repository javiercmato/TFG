package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PrivateListRecipeRepository extends PagingAndSortingRepository<PrivateListRecipe, PrivateListRecipeID> {
    String getRecipesFromPrivateListQuery =
            "SELECT r\n" +
            "FROM PrivateList pl LEFT JOIN PrivateListRecipe plr LEFT JOIN Recipe r\n" +
            "WHERE pl.id = ?1\n" +
            "ORDER BY plr.insertionDate DESC";
    
    String getRecipesFromPrivateListQueryCount =
            "SELECT count(*)\n" +
            "FROM PrivateList pl LEFT JOIN PrivateListRecipe plr LEFT JOIN Recipe r\n" +
            "WHERE pl.id = ?1\n" +
            "ORDER BY plr.insertionDate DESC";
    
    @Query(value = getRecipesFromPrivateListQuery,
        countQuery = getRecipesFromPrivateListQueryCount,
        nativeQuery = true
    )
    /** Devuelve las recetas que pertenecen a una lista privada, paginadas y ordenadas por fecha de inserción descendiente */
    Slice<Recipe> getRecipesFromPrivateList(UUID privateListID, Pageable pageable);
}
