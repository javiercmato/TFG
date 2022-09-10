package es.udc.fic.tfg.backendtfg.users.domain;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Rating;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserUnitTest {
    
    @Test
    void createUser() {
        // Crear datos de prueba
        UUID id = UUID.randomUUID();
        String nickname = "nickname";
        String password = "password";
        String name = "name";
        String surname = "surname";
        String email = "email";
        byte[] avatar = new byte[0];
        LocalDateTime registerDate = LocalDateTime.now();
        UserRole role = UserRole.USER;
        boolean isBannedByAdmin = false;
        Set<Comment> comments = new LinkedHashSet<>();
        Set<Rating> ratings = new LinkedHashSet<>();
        
        // Ejecutar código
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setEmail(email);
        user.setRole(role);
        user.setRegisterDate(registerDate);
        user.setBannedByAdmin(isBannedByAdmin);
        user.setComments(comments);
        user.setRatings(ratings);
        
        
        // Comprobar resultados
        assertAll(
                () -> assertEquals(id, user.getId()),
                () -> assertEquals(name, user.getName()),
                () -> assertEquals(surname, user.getSurname()),
                () -> assertEquals(nickname, user.getNickname()),
                () -> assertEquals(password, user.getPassword()),
                () -> assertEquals(avatar, user.getAvatar()),
                () -> assertEquals(email, user.getEmail()),
                () -> assertEquals(role, user.getRole()),
                () -> assertEquals(registerDate, user.getRegisterDate()),
                () -> assertEquals(isBannedByAdmin, user.isBannedByAdmin()),
                () -> assertEquals(comments, user.getComments()),
                () -> assertEquals(ratings, user.getRatings()),
                () -> assertTrue(user.getPrivateLists().isEmpty())
        );
    }
}
