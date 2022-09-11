package es.udc.fic.tfg.backendtfg.social.domain;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Follow;
import es.udc.fic.tfg.backendtfg.social.domain.entities.FollowID;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class FollowUnitTest {
    
    @Test
    void createFollow() {
        // Crear datos de prueba
        UUID followingID = UUID.randomUUID();
        UUID followedID = UUID.randomUUID();
        User followingUser = new User();
        User followedUser = new User();
        LocalDateTime followDate = LocalDateTime.now();
        
        // Ejecutar código
        FollowID followID = new FollowID(followingID, followedID);
        Follow follow = new Follow(followID, followDate, followingUser, followedUser);
        
        // Comprobar resultados
        assertAll(
                // Clave primaria compuesta está correctamente
                () -> assertEquals(followID, follow.getId()),
                () -> assertEquals(followedID, followID.getFollowed()),
                () -> assertEquals(followingID, followID.getFollowing()),
                // Datos son correctos
                () -> assertEquals(followDate, follow.getFollowDate()),
                () -> assertEquals(followingUser, follow.getFollowing()),
                () -> assertEquals(followedUser, follow.getFollowed())
        );
    }
}
