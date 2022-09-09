package es.udc.fic.tfg.backendtfg.social.domain.repositories;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.domain.entities.CommentID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CommentRepository extends PagingAndSortingRepository<Comment, CommentID> {
    
    /** Recupera los comentarios de una receta ordenados por fecha de creación descendiente */
    Slice<Comment> findById_RecipeIDOrderByCreationDateDesc(UUID recipeID, Pageable pageable);

}
