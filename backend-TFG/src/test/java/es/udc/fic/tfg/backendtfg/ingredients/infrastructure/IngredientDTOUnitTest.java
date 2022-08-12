package es.udc.fic.tfg.backendtfg.ingredients.infrastructure;

import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.DEFAULT_INGREDIENT_NAME;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class IngredientDTOUnitTest {
    @Test
    public void testIngredientDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        UUID typeID = UUID.randomUUID();
        UUID creatorID = UUID.randomUUID();
        
        // Ejecutar código
        IngredientDTO dto = new IngredientDTO();
        dto.setId(id);
        dto.setName(DEFAULT_INGREDIENT_NAME);
        dto.setIngredientTypeID(typeID);
        dto.setCreatorID(creatorID);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getId()),
                () -> assertEquals(DEFAULT_INGREDIENT_NAME, dto.getName())
        );
    }
    
    @Test
    public void testCreateIngredientParamsDTO() {
        // Crear datos de prueba
        UUID typeID = UUID.randomUUID();
        UUID creatorID = UUID.randomUUID();
        
        // Ejecutar código
        CreateIngredientParamsDTO dto = new CreateIngredientParamsDTO();
        dto.setName(DEFAULT_INGREDIENT_NAME);
        dto.setIngredientTypeID(typeID);
        dto.setCreatorID(creatorID);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(DEFAULT_INGREDIENT_NAME, dto.getName()),
                () -> assertEquals(creatorID, dto.getCreatorID()),
                () -> assertEquals(typeID, dto.getIngredientTypeID())
        );
    }
    
    @Test
    public void testIngredientSummaryDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        
        // Ejecutar código
        IngredientSummaryDTO dto = new IngredientSummaryDTO();
        dto.setName(DEFAULT_INGREDIENT_NAME);
        dto.setId(id);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getId()),
                () -> assertEquals(DEFAULT_INGREDIENT_NAME, dto.getName())
        );
    }
}
