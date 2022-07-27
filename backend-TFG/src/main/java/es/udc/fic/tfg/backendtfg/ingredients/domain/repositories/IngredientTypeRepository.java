package es.udc.fic.tfg.backendtfg.ingredients.domain.repositories;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface IngredientTypeRepository extends PagingAndSortingRepository<IngredientType, UUID> {

}
