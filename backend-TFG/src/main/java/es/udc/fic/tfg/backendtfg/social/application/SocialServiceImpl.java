package es.udc.fic.tfg.backendtfg.social.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.domain.repositories.RecipeRepository;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.domain.entities.CommentID;
import es.udc.fic.tfg.backendtfg.social.domain.repositories.CommentRepository;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SocialServiceImpl implements SocialService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private CommentRepository commentRepo;
    
    /* ******************** FUNCIONALIDADES COMENTARIOS ******************** */
    @Override
    public Comment commentRecipe(UUID userID, UUID recipeID, String text) throws EntityNotFoundException {
        // Buscar el usuario. Si no existe lanza EntityNotFoundException
        User author = userUtils.fetchUserByID(userID);
        // Buscar la receta. Si no existe lanza EntityNotFoundException
        Recipe recipe = fetchRecipeByID(recipeID);
        
        // Crear el comentario
        CommentID commentID = new CommentID(userID, recipeID);
        Comment comment = new Comment();
        comment.setId(commentID);
        comment.setCreationDate(LocalDateTime.now());
        comment.setText(text.trim());
        comment.setAuthor(author);
        
        // Guardar comentario y asignárselo a la receta
        comment = commentRepo.save(comment);
        recipe.addComment(comment);
        
        return comment;
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
}