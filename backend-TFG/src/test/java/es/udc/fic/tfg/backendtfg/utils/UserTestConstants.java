package es.udc.fic.tfg.backendtfg.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/** Utilidad para generar datos de usuario en los tests */
@UtilityClass
public class UserTestConstants {
    // INFO: https://stackoverflow.com/questions/64580412/generate-valid-deterministic-uuids-for-tests
    public static final UUID NON_EXISTENT_UUID = UUID.fromString("00000000-0000-4000-8000-000000000000");
    public static final String NON_EXISTENT_NICKNAME = "NON_EXISTENT_NICKNAME";
    public static final String DEFAULT_NAME = "name";
    public static final String DEFAULT_SURNAME = "surname";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_EMAIL_DOMAIN = "@email.es";
    public static final String DEFAULT_AVATAR_NAME = "default_user_avatar.png";
    public static final String ADMIN_NICKNAME = "admin";
}
