package es.udc.fic.tfg.backendtfg.social.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.PermissionException;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.RecipeRepository;
import es.udc.fic.tfg.backendtfg.social.domain.entities.*;
import es.udc.fic.tfg.backendtfg.social.domain.exceptions.RecipeAlreadyRatedException;
import es.udc.fic.tfg.backendtfg.social.domain.repositories.CommentRepository;
import es.udc.fic.tfg.backendtfg.social.domain.repositories.RatingRepository;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class SocialServiceImpl implements SocialService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private RatingRepository ratingRepo;
    
    
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
        
        // Recuperar toda la información de la receta
        return recipeRepo.retrieveRecipeDetails(recipeID).get();
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
