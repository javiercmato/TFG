package es.udc.fic.tfg.backendtfg.ingredients.domain.repositories;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, UUID> {
    
    @Query("select (count(i) > 0) from Ingredient i where upper(i.name) like upper(?1)")
    boolean existsByNameLikeIgnoreCase(String name);
    
    Optional<Ingredient> findByNameLikeIgnoreCase(String name);
    
    
}
