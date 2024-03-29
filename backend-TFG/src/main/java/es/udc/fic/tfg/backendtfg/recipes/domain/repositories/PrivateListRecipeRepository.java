package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface PrivateListRecipeRepository extends PagingAndSortingRepository<PrivateListRecipe, PrivateListRecipeID> {
    String getRecipesFromPrivateListQuery =
            "SELECT r \n" +
            "FROM PrivateListRecipe plr \n" +
                "JOIN plr.privateList pl \n" +
                "JOIN plr.recipe r \n" +
                "JOIN FETCH r.pictures pictures \n" +
            "WHERE pl.id = ?1 \n" +
            "ORDER BY plr.insertionDate DESC";
    
    String getRecipesFromPrivateListQueryCount = String.format(
            "SELECT count(*) FROM ({%s})",
            getRecipesFromPrivateListQuery
    );
    
    @Query(value = getRecipesFromPrivateListQuery
        //countQuery = getRecipesFromPrivateListQueryCount,
        // nativeQuery = true
    )
    /** Devuelve las recetas que pertenecen a una lista privada, paginadas y ordenadas por fecha de inserción descendiente */
    List<Recipe> getRecipesFromPrivateList(UUID privateListID);
}
