package es.udc.fic.tfg.backendtfg.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/** Utilidad para generar datos de usuario en los tests */
@UtilityClass
public class TestConstants {
    /* ************************* COMMON ************************* */
    // INFO: https://stackoverflow.com/questions/64580412/generate-valid-deterministic-uuids-for-tests
    public static final UUID NON_EXISTENT_UUID = UUID.fromString("00000000-0000-4000-8000-000000000000");
    
    /* ************************* USERS ************************* */
    public static final String NON_EXISTENT_NICKNAME = "NON_EXISTENT_NICKNAME";
    public static final String DEFAULT_NICKNAME = "nickname";
    public static final String DEFAULT_NAME = "name";
    public static final String DEFAULT_SURNAME = "surname";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_EMAIL_DOMAIN = "@email.es";
    public static final String DEFAULT_AVATAR_NAME = "default_user_avatar.png";
    public static final String ADMIN_NICKNAME = "admin";
    public static final String ADMIN_PASSWORD = "secretPassword";
    public static final String DEFAULT_PRIVATE_LIST_TITLE = "Private List";
    public static final String DEFAULT_PRIVATE_LIST_DESCRIPTION = "This is my private list description";
    
    
    /* ************************* INGREDIENTS ************************* */
    public static final String DEFAULT_INGREDIENT_NAME = "ingredient";
    public static final String DEFAULT_INGREDIENTTYPE_NAME = "INGREDIENT TYPE";
    public static final String VALID_INGREDIENTTYPE_NAME = "VALID INGREDIENT TYPE";
    
    
    /* ************************* RECIPES ************************* */
    public static final String DEFAULT_CATEGORY_NAME = "CATEGORY";
    public static final String DEFAULT_RECIPE_NAME = "recipe name";
    public static final Long DEFAULT_RECIPE_DURATION = (long) 10;
    public static final Integer DEFAULT_RECIPE_DINERS = 2;
    public static final String DEFAULT_RECIPE_DESCRIPTION = "This is my recipe description";
    public static final String DEFAULT_RECIPESTEP_TEXT = "This is a recipe step text";
    public static final String DEFAULT_RECIPE_IMAGE_1 = "foto_comida_1.png";
    
    /* ************************* SOCIAL ************************* */
    public static final String DEFAULT_COMMENT_TEXT = "This is a comment";
    public static final String DEFAULT_NOTIFICATION_TITLE = "Notification title";
    public static final String DEFAULT_NOTIFICATION_MESSAGE = "This is a notification";
    
}
