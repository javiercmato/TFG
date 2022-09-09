package es.udc.fic.tfg.backendtfg.social.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;

import java.util.UUID;

public interface SocialService {
    /**
     * Añade un comentario a una receta
     * @param userID ID del autor del comentario
     * @param recipeID ID de la receta a comentar
     * @param text Texto del comentario
     * @return Comentario realizado
     * @throws EntityNotFoundException No se encuentra al autor o a la receta
     */
    Comment commentRecipe(UUID userID, UUID recipeID, String text) throws EntityNotFoundException;
    
    /**
     * Recupera los comentarios de una receta paginados.
     * @param recipeID ID de la receta
     * @param page Número de página
     * @param pageSize Tamaño de la página
     * @return Bloque de comentarios
     * @throws EntityNotFoundException NO se encuentra la receta
     */
    Block<Comment> getRecipeComments(UUID recipeID, int page, int pageSize) throws EntityNotFoundException;
    
}
