package es.udc.fic.tfg.backendtfg.social.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.PermissionException;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.RecipeRepository;
import es.udc.fic.tfg.backendtfg.social.domain.entities.*;
import es.udc.fic.tfg.backendtfg.social.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.social.domain.repositories.*;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class SocialServiceImpl implements SocialService {
    public static final Locale locale = new Locale("es");
    
    /* ******************** TRADUCCIONES DE MENSAJES ******************** */
    public static final String NEW_RECIPE_TITLE         = "social.notification.newRecipe.title";
    public static final String NEW_RECIPE_BODY          = "social.notification.newRecipe.body";
    public static final String NEW_FOLLOWER_TITLE       = "social.notification.newFollower.title";
    public static final String NEW_FOLLOWER_BODY        = "social.notification.newFollower.body";
    public static final String NEW_COMMENT_TITLE        = "social.notification.recipeComment.title";
    public static final String NEW_COMMENT_BODY         = "social.notification.recipeComment.body";
    public static final String NEW_RATING_TITLE         = "social.notification.recipeRating.title";
    public static final String NEW_RATING_BODY          = "social.notification.recipeRating.body";
    
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private RatingRepository ratingRepo;
    @Autowired
    private FollowRepository followRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private NotificationRepository notificationRepo;
    @Autowired
    private SocialService socialService;
    @Autowired
    private MessageSource messageSource;
    
    
    /* ******************** FUNCIONALIDADES COMENTARIOS ******************** */
    @Override
    public Comment commentRecipe(UUID userID, UUID recipeID, String text) throws EntityNotFoundException {
        // Buscar el usuario. Si no existe lanza EntityNotFoundException
        User author = userUtils.fetchUserByID(userID);
        // Buscar la receta. Si no existe lanza EntityNotFoundException
        Recipe recipe = fetchRecipeByID(recipeID);
        
        // Crear el comentario
        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment(now, text.trim(), false, author, recipe);
        
        // Guardar comentario y asignárselo a la receta
        comment = commentRepo.save(comment);
        recipe.addComment(comment);
        
        // Notificar al propietario de la receta de que tiene un comentario nuevo
        try {
            // Crear título y cuerpo del mensaje
            String recipeName = recipe.getName();
            String title = messageSource.getMessage(NEW_COMMENT_TITLE, null, locale);
            String message = messageSource.getMessage(NEW_COMMENT_BODY, new Object[] {recipeName}, locale);
            UUID recipeOwnerID = recipe.getAuthor().getId();
        
            // Crear y guardar notificación
            socialService.createNotification(title, message, recipeOwnerID);
        } catch ( EntityNotFoundException e ) {
            throw new RuntimeException(e);
        }
        
        return comment;
    }
    
    @Override
    public Block<Comment> getRecipeComments(UUID recipeID, int page, int pageSize) throws EntityNotFoundException {
        // Si no existe la receta lanza EntityNotFoundException
        if (!recipeRepo.existsById(recipeID))
            throw new EntityNotFoundException(Recipe.class.getSimpleName(), recipeID);
        
        // Buscar los comentarios
        Pageable pageable = PageRequest.of(page, pageSize);
        Slice<Comment> commentsSlice = commentRepo.findByRecipe_IdOrderByCreationDateDesc(recipeID, pageable);
        
        return new Block<>(commentsSlice.getContent(), commentsSlice.hasNext(), commentsSlice.getNumberOfElements());
    }
    
    @Override
    public Comment banCommentAsAdmin(UUID adminID, UUID commentID) throws EntityNotFoundException, PermissionException {
        // Commprobar si existe el administrador
        try {
            userUtils.fetchAdministrator(adminID);
        } catch ( EntityNotFoundException ex) {
            throw new PermissionException();
        }
        
        // Obtener el comentario a banear
        Comment targetComment = fetchCommentByID(commentID);
        
        // Aplicar/retirar baneo
        targetComment.setBannedByAdmin(!targetComment.isBannedByAdmin());
        
        return commentRepo.save(targetComment);
    }
    
    @Override
    public Recipe rateRecipe(UUID userID, UUID recipeID, int value) throws EntityNotFoundException, RecipeAlreadyRatedException {
        // Buscar el usuario. Si no existe lanza EntityNotFoundException
        User author = userUtils.fetchUserByID(userID);
        // Buscar la receta. Si no existe lanza EntityNotFoundException
        Recipe recipe = fetchRecipeByID(recipeID);
        
        // Comprobar que la receta no haya sido puntuada ya por el usuario. Sino lanza RecipeAlreadyRatedException
        RatingID ratingID = new RatingID(author.getId(), recipe.getId());
        if (ratingRepo.existsById(ratingID))
            throw new RecipeAlreadyRatedException();
        
        // Puntuar la receta
        Rating rating = new Rating();
        rating.setId(ratingID);
        rating.setValue(value);
        
        // Guardar puntuación y asignárselo a receta e indicar usuario que puntúa
        author.addRating(rating);
        recipe.rate(rating);
        ratingRepo.save(rating);
        recipeRepo.save(recipe);
    
        // Notificar al propietario de la receta de que tiene una nueva puntuación
        try {
            // Crear título y cuerpo del mensaje
            String title = messageSource.getMessage(NEW_RATING_TITLE, null, locale);
            String message = messageSource.getMessage(NEW_RATING_BODY, new Object[] {recipe.getName()}, locale);
            UUID recipeOwnerID = recipe.getAuthor().getId();
        
            // Crear y guardar notificación
            socialService.createNotification(title, message, recipeOwnerID);
        } catch ( EntityNotFoundException e ) {
            throw new RuntimeException(e);
        }
        
        // Recuperar toda la información de la receta
        return recipeRepo.retrieveRecipeDetails(recipeID).get();
    }
    
    @Override
    public Follow followUser(UUID requestorID, UUID targetID)
            throws EntityNotFoundException, UserAlreadyFollowedException {
        // Buscar el usuario actual. Si no existe lanza EntityNotFoundException
        User requestor = userUtils.fetchUserByID(requestorID);
        // Buscar el usuario objetivo. Si no existe lanza EntityNotFoundException
        User target = userUtils.fetchUserByID(targetID);
        
        // Comprobar que no esté siguiendo al usuario objetivo. Sino lanza UserAlreadyFollowedException
        FollowID followID = new FollowID(requestorID, targetID);
        if (followRepo.existsById(followID))
            throw new UserAlreadyFollowedException(target.getNickname());
        
        // Crear la relación entre los usuarios
        Follow follow = new Follow();
        follow.setId(followID);
        follow.setFollowDate(LocalDateTime.now());
        
        // Indicar a los usuarios que se están siguiendo
        requestor.addFollowing(follow);
        target.addFollower(follow);
        
        // Guardar cambios y devolver relación
        follow = followRepo.save(follow);
        userRepo.save(requestor);
        userRepo.save(target);
    
        // Notificar al usuario de que tiene un nuevo seguidor
        try {
            // Crear título y cuerpo del mensaje
            String title = messageSource.getMessage(NEW_FOLLOWER_TITLE, null, locale);
            String message = messageSource.getMessage(NEW_FOLLOWER_BODY, null, locale);
        
            // Crear y guardar notificación
            socialService.createNotification(title, message, targetID);
        } catch ( EntityNotFoundException e ) {
            throw new RuntimeException(e);
        }
        
        return follow;
    }
    
    @Override
    public void unfollowUser(UUID requestorID, UUID targetID) throws EntityNotFoundException, UserNotFollowedException {
        // Buscar el usuario actual. Si no existe lanza EntityNotFoundException
        User requestor = userUtils.fetchUserByID(requestorID);
        // Buscar el usuario objetivo. Si no existe lanza EntityNotFoundException
        User target = userUtils.fetchUserByID(targetID);
    
        // Comprobar que sí esté siguiendo al usuario objetivo
        FollowID followID = new FollowID(requestorID, targetID);
        Optional<Follow> optionalFollow = followRepo.findById(followID);
        if ( optionalFollow.isEmpty() ) {
            throw new UserNotFollowedException(target.getNickname());
        }
        Follow follow = optionalFollow.get();
        
        // Indicar a los usuarios que se dejan de seguir
        target.removeFollower(follow);
        requestor.removeFollowing(follow);
        
        // Guardar cambios y borrar relación
        userRepo.save(target);
        userRepo.save(requestor);
        followRepo.delete(follow);
    }
    
    @Override
    public Block<Follow> getFollowers(UUID userID, int page, int pageSize) throws EntityNotFoundException {
        // Buscar el usuario actual. Si no existe lanza EntityNotFoundException
        userUtils.fetchUserByID(userID);
        
        // Buscar los seguidores
        Pageable pageable = PageRequest.of(page, pageSize);
        Slice<Follow> followersSlice = followRepo.findFollowers(userID, pageable);
        
        return new Block<>(followersSlice.getContent(), followersSlice.hasNext(), followersSlice.getNumberOfElements());
    }
    
    @Override
    public Block<Follow> getFollowings(UUID userID, int page, int pageSize) throws EntityNotFoundException {
        // Buscar el usuario actual. Si no existe lanza EntityNotFoundException
        userUtils.fetchUserByID(userID);
    
        // Buscar los seguidores
        Pageable pageable = PageRequest.of(page, pageSize);
        Slice<Follow> followersSlice = followRepo.findFollowings(userID, pageable);
    
        return new Block<>(followersSlice.getContent(), followersSlice.hasNext(), followersSlice.getNumberOfElements());
    }
    
    @Override
    public boolean doesFollowTarget(UUID requestorID, UUID targetID) {
        FollowID id = new FollowID(requestorID, targetID);
        
        return followRepo.existsById(id);
    }
    
    @Override
    public Notification createNotification(String title, String message, UUID targetUserID)
            throws EntityNotFoundException {
        // Buscar el usuario objetivo. Si no existe lanza EntityNotFoundException
        User targetUser = userUtils.fetchUserByID(targetUserID);
        
        // Crear la notificación
        Notification notification = new Notification();
        notification.setTarget(targetUser);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setTitle(title.trim());
        notification.setMessage(message.trim());
        
        return notificationRepo.save(notification);
    }
    
    @Override
    public Block<Notification> getUnreadNotifications(UUID targetUserID, int page, int pageSize)
            throws EntityNotFoundException {
        // Buscar el usuario objetivo. Si no existe lanza EntityNotFoundException
        userUtils.fetchUserByID(targetUserID);
        
        // Buscar las notificaciones
        Pageable pageable = PageRequest.of(page, pageSize);
        Slice<Notification> slice = notificationRepo.findByTarget_IdAndIsReadFalseOrderByCreatedAtDesc(targetUserID, pageable);
        
        return new Block<>(slice.getContent(), slice.hasNext(), slice.getNumberOfElements());
    }
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    /** Busca la receta por el ID recibido */
    private Recipe fetchRecipeByID(UUID recipeID) throws EntityNotFoundException {
        Optional<Recipe> optionalRecipe = recipeRepo.findById(recipeID);
        if (!optionalRecipe.isPresent()) {
            throw new EntityNotFoundException(Recipe.class.getSimpleName(), recipeID);
        }
        
        return optionalRecipe.get();
    }
    
    /** Busca el comentario por el ID recibido */
    private Comment fetchCommentByID(UUID commentID) throws EntityNotFoundException {
        Optional<Comment> optionalComment = commentRepo.findById(commentID);
        if (!optionalComment.isPresent()) {
            throw new EntityNotFoundException(Comment.class.getSimpleName(), commentID);
        }
        
        return optionalComment.get();
    }
}
