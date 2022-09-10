package es.udc.fic.tfg.backendtfg.social.domain.repositories;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CommentRepository extends PagingAndSortingRepository<Comment, UUID> {
    
    /** Recupera los comentarios de una receta ordenados por fecha de creación descendiente */
    Slice<Comment> findByRecipe_IdOrderByCreationDateDesc(UUID recipeID, Pageable pageable);
    
    
}
