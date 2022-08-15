package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.RecipePicture;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.RecipePictureID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipePictureRepository extends PagingAndSortingRepository<RecipePicture, RecipePictureID> {
    
}
