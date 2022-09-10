package es.udc.fic.tfg.backendtfg.social.domain.repositories;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Rating;
import es.udc.fic.tfg.backendtfg.social.domain.entities.RatingID;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, RatingID> {

}
