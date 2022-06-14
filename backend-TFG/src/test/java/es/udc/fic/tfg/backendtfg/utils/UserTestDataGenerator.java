package es.udc.fic.tfg.backendtfg.utils;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.*;

/** Utilidad para generar datos de usuario en los tests */
@UtilityClass
public class UserTestDataGenerator {
    // INFO: https://stackoverflow.com/questions/64580412/generate-valid-deterministic-uuids-for-tests
    public static final UUID NON_EXISTENT_UUID = UUID.fromString("00000000-0000-4000-8000-000000000000");
    public static final String NON_EXISTENT_NICKNAME = "NON_EXISTENT_NICKNAME";
    public static final String DEFAULT_NAME = "name";
    public static final String DEFAULT_SURNAME = "surname";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_EMAIL_DOMAIN = "@email.es";
    public static final String DEFAULT_AVATAR_NAME = "default_user_avatar.png";
    
    /** Genera datos de un usuario válido. */
    public static User generateValidUser(String nickname) {
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(DEFAULT_PASSWORD);
        user.setName(DEFAULT_NAME);
        user.setSurname(DEFAULT_SURNAME);
        user.setEmail(nickname + DEFAULT_EMAIL_DOMAIN);
        user.setRole(UserRole.USER);
        user.setBannedByAdmin(false);
        user.setRegisterDate(LocalDateTime.now());
        user.setAvatar(loadImageFromResourceName(DEFAULT_NAME, PNG_EXTENSION));
        
        return user;
    }
    
}
