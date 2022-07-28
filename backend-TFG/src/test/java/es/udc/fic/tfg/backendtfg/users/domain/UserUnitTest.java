package es.udc.fic.tfg.backendtfg.users.domain;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserUnitTest {
    
    @Test
    public void createUser() {
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
                () -> assertTrue(user.getPrivateLists().isEmpty())
        );
        
    }
}
