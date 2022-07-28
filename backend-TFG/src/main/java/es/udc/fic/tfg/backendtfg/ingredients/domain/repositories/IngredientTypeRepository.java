package es.udc.fic.tfg.backendtfg.ingredients.domain.repositories;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface IngredientTypeRepository extends CrudRepository<IngredientType, UUID> {

}
