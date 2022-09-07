package es.udc.fic.tfg.backendtfg.ingredients.domain;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class IngredientUnitTest {
    
    @Test
    public void createIngredient() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        String name = "ingredient";
        IngredientType type = new IngredientType();
        type.setId(UUID.randomUUID());
        type.setName("IngredientType");
        User creator = new User();
        

        // Ejecutar código
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName(name);
        ingredient.setIngredientType(type);
        ingredient.setCreator(creator);
        ingredient.setRecipeIngredients(Collections.emptyList());
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, ingredient.getId()),
                () -> assertEquals(name, ingredient.getName()),
                () -> assertEquals(type, ingredient.getIngredientType()),
                () -> assertEquals(creator, ingredient.getCreator()),
                () -> assertTrue(ingredient.getRecipeIngredients().isEmpty())
        );
    }
}
