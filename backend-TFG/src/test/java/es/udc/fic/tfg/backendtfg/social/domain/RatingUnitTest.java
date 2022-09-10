package es.udc.fic.tfg.backendtfg.social.domain;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Rating;
import es.udc.fic.tfg.backendtfg.social.domain.entities.RatingID;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RatingUnitTest {

    @Test
    void createRating() {
        // Crear datos de prueba
        UUID authorID = UUID.randomUUID();
        UUID recipeID = UUID.randomUUID();
        int ratingValue = 10;
        User author = new User();
        author.setId(authorID);
        Recipe recipe = new Recipe();
        recipe.setId(recipeID);
        
        // Ejecutar código
        RatingID ratingID = new RatingID(authorID, recipeID);
        Rating rating = new Rating(ratingID, ratingValue, recipe, author);
    
        // Comprobar resultados
        assertAll(
                // Clave primaria compuesta está correctamente
                () -> assertEquals(ratingID, rating.getId()),
                () -> assertEquals(recipeID, ratingID.getRecipeID()),
                () -> assertEquals(authorID, ratingID.getAuthorID()),
                // Datos son correctos
                () -> assertEquals(ratingValue, rating.getValue())
        );
    }
    
    @Test
    void createRatingAndAttachToUser() {
        // Crear datos de prueba
        UUID authorID = UUID.randomUUID();
        UUID recipeID = UUID.randomUUID();
        int ratingValue = 10;
        User author = new User();
        Recipe recipe = new Recipe();
        
        // Ejecutar código
        RatingID ratingID = new RatingID(authorID, recipeID);
        Rating rating = new Rating(ratingID, ratingValue, recipe, author);
        author.addRating(rating);
        recipe.rate(rating);
        
        // Comprobar resultados
        assertAll(
                () -> assertNotNull(rating),
                () -> assertEquals(author, rating.getAuthor()),
                () -> assertEquals(recipe, rating.getRecipe())
        );
    }
}
