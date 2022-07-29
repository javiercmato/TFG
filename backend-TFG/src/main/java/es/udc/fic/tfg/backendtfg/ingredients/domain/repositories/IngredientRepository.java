package es.udc.fic.tfg.backendtfg.ingredients.domain.repositories;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository extends CrudRepository<Ingredient, UUID> {
    
    @Query("select (count(i) > 0) from Ingredient i where upper(i.name) like upper(?1)")
    boolean existsByNameLikeIgnoreCase(String name);
    
    Optional<Ingredient> findByNameLikeIgnoreCase(String name);
    
    Slice<Ingredient> findByNameContainsIgnoreCaseOrderByNameAsc(String name, Pageable pageable);
    
    Slice<Ingredient> findByIngredientType_IdOrderByNameAsc(UUID id, Pageable pageable);
    
}
