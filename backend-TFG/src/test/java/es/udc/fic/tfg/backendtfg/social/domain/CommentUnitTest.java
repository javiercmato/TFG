package es.udc.fic.tfg.backendtfg.social.domain;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.DEFAULT_COMMENT_TEXT;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CommentUnitTest {
    
    @Test
    void createComment() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        LocalDateTime creationDate = LocalDateTime.now();
        boolean isBannedByAdmin = false;
        User author = new User();
        Recipe recipe = new Recipe();
        
        // Ejecutar código
        Comment comment = new Comment();
        comment.setId(id);
        comment.setCreationDate(creationDate);
        comment.setText(DEFAULT_COMMENT_TEXT);
        comment.setBannedByAdmin(isBannedByAdmin);
        comment.setAuthor(author);
        comment.setRecipe(recipe);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, comment.getId()),
                () -> assertEquals(creationDate, comment.getCreationDate()),
                () -> assertEquals(DEFAULT_COMMENT_TEXT, comment.getText()),
                () -> assertFalse(comment.isBannedByAdmin()),
                () -> assertEquals(author, comment.getAuthor()),
                () -> assertEquals(recipe, comment.getRecipe())
        );
    }
    
    @Test
    void createCommentAndAttachToRecipe() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        LocalDateTime creationDate = LocalDateTime.now();
        boolean isBannedByAdmin = false;
        User author = new User();
        Recipe recipe = new Recipe();
        
        // Ejecutar código
        Comment comment = new Comment(creationDate, DEFAULT_COMMENT_TEXT, isBannedByAdmin, author, recipe);
        comment.attachToRecipe(recipe);
        
        // Comprobar resultados
        assertAll(
                () -> assertNotNull(comment),
                () -> assertEquals(recipe, comment.getRecipe())
        );
    }
}
