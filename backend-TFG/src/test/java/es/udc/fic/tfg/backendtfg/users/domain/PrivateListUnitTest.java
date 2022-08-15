package es.udc.fic.tfg.backendtfg.users.domain;

import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                () -> assertEquals(creator, privateList.getCreator())
        );
        
    }
}
