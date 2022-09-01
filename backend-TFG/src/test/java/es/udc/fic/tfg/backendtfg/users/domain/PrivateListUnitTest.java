package es.udc.fic.tfg.backendtfg.users.domain;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PrivateListUnitTest {
    
    @Test
    void createPrivateList() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        String title = "title";
        String description = "My private list of recipes";
        User creator = new User();
        
        // Ejecutar código
        PrivateList privateList = new PrivateList();
        privateList.setId(id);
        privateList.setTitle(title);
        privateList.setDescription(description);
        privateList.setCreator(creator);
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, privateList.getId()),
                () -> assertEquals(title, privateList.getTitle()),
                () -> assertEquals(description, privateList.getDescription()),
                () -> assertEquals(creator, privateList.getCreator()),
                () -> assertTrue(privateList.getPrivateListRecipes().isEmpty())
        );
    }
    
    @Test
    void createPrivateListAndAddRecipe() {
        // Crear datos de prueba
        UUID privateListID = UUID.randomUUID();
        Recipe recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        PrivateListRecipe plr = new PrivateListRecipe();
        plr.setId(new PrivateListRecipeID(privateListID, recipe.getId()));
    
        // Ejecutar código
        PrivateList privateList = new PrivateList();
        privateList.setId(privateListID);
        privateList.insertRecipe(plr);
        recipe.insertToPrivateList(plr);
    
        // Comprobar resultados
        assertAll(
                // Hay recetas añadidas a la lista
                () -> assertFalse(privateList.getPrivateListRecipes().isEmpty()),
                // Los datos son correctos
                () -> assertEquals(recipe, plr.getRecipe()),
                () -> assertEquals(privateList, plr.getPrivateList())
        );
    }
}
