package es.udc.fic.tfg.backendtfg.social.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.PermissionException;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Follow;
import es.udc.fic.tfg.backendtfg.social.domain.exceptions.*;

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
    
    /**
     * Banea un comentario del sistema.
     * @param adminID ID del administrador
     * @param commentID ID del comentario a banear
     * @return True si el comentario está baneado; false si no lo está
     * @throws EntityNotFoundException No se encuentra el comentario
     * @throws PermissionException El usuario no es administrador
     */
    Comment banCommentAsAdmin(UUID adminID, UUID commentID) throws EntityNotFoundException, PermissionException;
    
    /**
     * Permite puntuar una receta.
     * @param userID ID del usuario que puntúa
     * @param recipeID ID de la receta a puntuar
     * @param value Puntuación a añadir (>= 0)
     * @return Receta con la puntuación actualizada
     * @throws EntityNotFoundException No se encuentra la receta o el usuario
     * @throws RecipeAlreadyRatedException Receta ya ha sido puntuada por el usuario
     */
    Recipe rateRecipe(UUID userID, UUID recipeID, int value) throws EntityNotFoundException, RecipeAlreadyRatedException;
    
    /**
     * Sigue a un usuario.
     * @param requestorID ID del usuario que solicita la operación de seguir
     * @param targetID ID del usuario que se desea seguir
     * @return Relación de seguimiento entre usuarios
     * @throws EntityNotFoundException No se encuentra alguno de los usuarios
     * @throws UserAlreadyFollowedException El usuario objetivo ya está siendo seguido por el usuario actual
     */
    Follow followUser(UUID requestorID, UUID targetID) throws EntityNotFoundException, UserAlreadyFollowedException;
    
    /**
     * Dejar de seguir a un usuario.
     * @param requestorID ID del usuario que solicita la operación de dejar de seguir
     * @param targetID ID del usuario que se desea dejar de seguir
     * @return Relación de seguimiento entre usuarios
     * @throws EntityNotFoundException No se encuentra alguno de los usuarios
     * @throws UserNotFollowedException El usuario objetivo no estaba siendo seguido
     */
    void unfollowUser(UUID requestorID, UUID targetID) throws EntityNotFoundException, UserNotFollowedException;
    
    /**
     * Recupera los seguidores del usuario recibido.
     * @param userID ID del usuario que desea recuperar sus seguidores
     * @param page Número de página
     * @param pageSize Tamaño de la página
     * @return Bloque de seguidores del usuario
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    Block<Follow> getFollowers(UUID userID, int page, int pageSize) throws EntityNotFoundException;
    
    /**
     * Recupera los usuarios a los que sigue el usuario recibido
     * @param userID ID del usuario que desea recuperar sus siguiendos
     * @param page Número de página
     * @param pageSize Tamaño de la página
     * @return Bloque de siguiendos del usuario
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    Block<Follow> getFollowings(UUID userID, int page, int pageSize) throws EntityNotFoundException;
    
    /**
     * Indica si un usuario está siguiento a otro.
     * @param requestorID ID del usuario que realiza la consulta
     * @param targetID ID del usuario objetivo
     * @return True si Requestor sigue a Target
     */
    boolean doesFollowTarget(UUID requestorID, UUID targetID);
}
