package es.udc.fic.tfg.backendtfg.ingredients.domain.repositories;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import org.springframework.data.repository.CrudRepository;

import java.util.*;


public interface IngredientTypeRepository extends CrudRepository<IngredientType, UUID> {

    boolean existsByNameIgnoreCase(String name);
    
    Optional<IngredientType> findIngredientTypeByNameIgnoreCase(String name);
    
    List<IngredientType> findByOrderByNameAsc();

}
