package es.udc.fic.tfg.backendtfg.recipes.infrastructure;

import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.DEFAULT_CATEGORY_NAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CategoryDTOUnitTest {
    @Test
    public void testCategoryDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        
        // Ejecutar código
        CategoryDTO dto = new CategoryDTO();
        dto.setId(id);
        dto.setName(DEFAULT_CATEGORY_NAME);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getId()),
                () -> assertEquals(DEFAULT_CATEGORY_NAME, dto.getName())
        );
    }
    
    @Test
    public void testCreateCategoryParamsDTO() {
        // Ejecutar código
        CategoryDTO dto = new CategoryDTO();
        dto.setName(DEFAULT_CATEGORY_NAME);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_CATEGORY_NAME, dto.getName())
        );
    }
    
}
