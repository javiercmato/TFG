package es.udc.fic.tfg.backendtfg.recipes.domain;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryUnitTest {
    
    @Test
    void createCategory() {
        // Crear datos de prueba
        UUID categoryID = UUID.randomUUID();
        String name = "category";
        
        // Ejecutar código
        Category category = new Category();
        category.setId(categoryID);
        category.setName(name);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(categoryID, category.getId()),
                () -> assertEquals(name, category.getName())
        );
    }
}
