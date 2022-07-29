package es.udc.fic.tfg.backendtfg.ingredients.domain.repositories;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;


public interface IngredientTypeRepository extends PagingAndSortingRepository<IngredientType, UUID> {

    boolean existsByNameIgnoreCase(String name);
    
    Optional<IngredientType> findIngredientTypeByNameIgnoreCase(String name);

}
