package es.udc.fic.tfg.backendtfg.ingredients.infrastructure;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.CreateIngredientTypeParamsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.DEFAULT_INGREDIENTTYPE_NAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class IngredientTypeDTOUnitTest {
    @Test
    public void testIngredientTypeDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        
        // Ejecutar código
        IngredientType dto = new IngredientType();
        dto.setId(id);
        dto.setName(DEFAULT_INGREDIENTTYPE_NAME);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getId()),
                () -> assertEquals(DEFAULT_INGREDIENTTYPE_NAME, dto.getName())
        );
    }
    
    @Test
    public void testCreateIngredientTypeParamsDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        
        // Ejecutar código
        CreateIngredientTypeParamsDTO dto = new CreateIngredientTypeParamsDTO();
        dto.setName(DEFAULT_INGREDIENTTYPE_NAME);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_INGREDIENTTYPE_NAME, dto.getName())
        );
    }
}
