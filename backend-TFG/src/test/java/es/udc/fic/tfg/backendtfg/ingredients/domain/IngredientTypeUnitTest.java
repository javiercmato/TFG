package es.udc.fic.tfg.backendtfg.ingredients.domain;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class IngredientTypeUnitTest {
    
    @Test
    void createIngredientType() {
        // Crear datos de prueba
        UUID ingredientTypeID = UUID.randomUUID();
        String ingredientTypeName = "ingredientType";
        
        
        // Ejecutar código
        IngredientType ingredientType = new IngredientType();
        ingredientType.setId(ingredientTypeID);
        ingredientType.setName(ingredientTypeName);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(ingredientTypeID, ingredientType.getId()),
                () -> assertEquals(ingredientTypeName, ingredientType.getName())
        );
        
    }
}
