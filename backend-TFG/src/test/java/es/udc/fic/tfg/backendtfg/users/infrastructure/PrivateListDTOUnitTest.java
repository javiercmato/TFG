package es.udc.fic.tfg.backendtfg.users.infrastructure;

import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.RecipeSummaryDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.PrivateListDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.PrivateListSummaryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static es.udc.fic.tfg.backendtfg.utils.TestConstants.DEFAULT_PRIVATE_LIST_DESCRIPTION;
import static es.udc.fic.tfg.backendtfg.utils.TestConstants.DEFAULT_PRIVATE_LIST_TITLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PrivateListDTOUnitTest {
    @Test
    void testPrivateListDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        List<RecipeSummaryDTO> recipesList = Collections.emptyList();
        
        // Ejecutar código
        PrivateListDTO dto = new PrivateListDTO();
        dto.setId(id);
        dto.setTitle(DEFAULT_PRIVATE_LIST_TITLE);
        dto.setDescription(DEFAULT_PRIVATE_LIST_DESCRIPTION);
        dto.setRecipes(recipesList);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getId()),
                () -> assertEquals(DEFAULT_PRIVATE_LIST_TITLE, dto.getTitle()),
                () -> assertEquals(DEFAULT_PRIVATE_LIST_DESCRIPTION, dto.getDescription()),
                () -> assertTrue(recipesList.isEmpty())
        );
    }
    
    @Test
    void testPrivateListSummaryDTO() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        
        // Ejecutar código
        PrivateListSummaryDTO dto = new PrivateListSummaryDTO(id, DEFAULT_PRIVATE_LIST_TITLE);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, dto.getId()),
                () -> assertEquals(DEFAULT_PRIVATE_LIST_TITLE, dto.getTitle())
        );
    }
}
