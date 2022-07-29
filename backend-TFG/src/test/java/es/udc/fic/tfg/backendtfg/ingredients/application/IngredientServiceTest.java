package es.udc.fic.tfg.backendtfg.ingredients.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.ingredients.application.services.IngredientService;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientRepository;
import es.udc.fic.tfg.backendtfg.ingredients.domain.repositories.IngredientTypeRepository;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.PNG_EXTENSION;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.loadImageFromResourceName;
import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IngredientServiceTest {
    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private IngredientTypeRepository ingredientTypeRepository;
    
    @Autowired
    private IngredientRepository ingredientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /* ************************* MÉTODOS AUXILIARES ************************* */
    /** Genera datos de un usuario válido. */
    private User generateValidUser() {
        User user = new User();
        user.setNickname(DEFAULT_NICKNAME);
        user.setPassword(DEFAULT_PASSWORD);
        user.setName(DEFAULT_NAME);
        user.setSurname(DEFAULT_SURNAME);
        user.setEmail(user.getNickname() + DEFAULT_EMAIL_DOMAIN);
        user.setRole(UserRole.USER);
        user.setBannedByAdmin(false);
        user.setRegisterDate(LocalDateTime.now());
        user.setAvatar(loadImageFromResourceName(DEFAULT_AVATAR_NAME, PNG_EXTENSION));
        user.setPrivateLists(Collections.emptySet());
        
        return userRepository.save(user);
    }
    
    /** Recupera al administrador */
    private User findAdmin() {
        Iterable<User> iterableUsers = userRepository.findAll();
        User admin = null;
        // Recorre todos los usuarios
        for (User user : iterableUsers) {
            // Si el usuario es ADMIN, lo devuelve
            if (user.getRole().equals(UserRole.ADMIN)) {
                admin = user;
            }
        }
        
        return admin;
    }
    
    /** Genera datos de un ingrediente válido. */
    private Ingredient generateValidIngredient(String ingredientName) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName(ingredientName);
        
        return ingredient;
    }
    
    /** Genera datos de un tipo de ingrediente válido. */
    private IngredientType generateValidIngredientType(String ingredientTypeName) {
        IngredientType ingredientType = new IngredientType();
        ingredientType.setName(ingredientTypeName);
        
        return ingredientType;
    }
    
    /* ************************* CASOS DE PRUEBA ************************* */
    
    @Test
    void whenCreateIngredient_thenNewIngredientIsCreated()
            throws EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        User user = generateValidUser();
        Ingredient validIngredient = generateValidIngredient(DEFAULT_INGREDIENT_NAME);
        IngredientType validIngredientType = ingredientTypeRepository.save(generateValidIngredientType(DEFAULT_INGREDIENTTYPE_NAME));
        validIngredient.setIngredientType(validIngredientType);
        validIngredient.setCreator(user);
        
        // Ejecutar funcionalidades
        Ingredient createdIngredient = ingredientService.createIngredient(
            DEFAULT_INGREDIENT_NAME,
            validIngredientType.getId(),
            user.getId()
        );
        
        // Comprobar resultados
        assertAll(
            // Ingrediente se ha registrado
            () -> assertNotNull(createdIngredient),
            // Datos son los mismos
            () -> assertEquals(validIngredient.getName(), createdIngredient.getName()),
            () -> assertEquals(user, createdIngredient.getCreator()),
            () -> assertEquals(validIngredientType, validIngredient.getIngredientType())
        );
    }
    
    @Test
    void whenCreateIngredientTwice_thenEntityAlreadyExistsException()
            throws EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        User user = generateValidUser();
        Ingredient validIngredient = generateValidIngredient(DEFAULT_INGREDIENT_NAME);
        IngredientType validIngredientType = ingredientTypeRepository.save(generateValidIngredientType(DEFAULT_INGREDIENTTYPE_NAME));
        validIngredient.setIngredientType(validIngredientType);
        validIngredient.setCreator(user);
        
        // Ejecutar funcionalidades
        Ingredient createdIngredient = ingredientService.createIngredient(
                DEFAULT_INGREDIENT_NAME,
                validIngredientType.getId(),
                user.getId()
        );
        
        // Comprobar resultados
        assertAll(
            // Ingrediente se ha registrado
            () -> assertNotNull(createdIngredient),
            // Datos son los mismos
            () -> assertEquals(validIngredient.getName(), createdIngredient.getName()),
            // Lanza excepción al crear ingrediente repetido
            () -> assertThrows(EntityAlreadyExistsException.class,
                () -> ingredientService.createIngredient(DEFAULT_INGREDIENT_NAME, validIngredientType.getId(), user.getId())
            )
        );
    }
    
    @Test
    void whenCreateIngredient_andTypeDoesNotExist_thenEntityNotFoundException() {
        // Crear datos de prueba
        User user = generateValidUser();
        Ingredient validIngredient = generateValidIngredient(DEFAULT_INGREDIENT_NAME);
        validIngredient.setCreator(user);
        
        // Comprobar resultados
        assertAll(
            // Lanza excepción al crear ingrediente
            () -> assertThrows(EntityNotFoundException.class,
                () -> ingredientService.createIngredient(DEFAULT_INGREDIENT_NAME, NON_EXISTENT_UUID, user.getId())
            )
        );
    }
    
    @Test
    void whenCreateIngredientType_thenNewIngredientTypeIsCreated()
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        User admin = findAdmin();
        
        // Ejecutar funcionalidades
        IngredientType createdType = ingredientService.createIngredientTypeAsAdmin(DEFAULT_INGREDIENTTYPE_NAME, admin.getId());
        
        // Comprobar resultados
        assertAll(
            // Tipo de ingrediente se ha registrado
            () -> assertNotNull(createdType),
            // Datos son los mismos
            () -> assertEquals(DEFAULT_INGREDIENTTYPE_NAME, createdType.getName())
        );
    }
    
    @Test
    void whenCreateIngredientTypeTwice_thenEntityAlreadyExistsException()
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        User admin = findAdmin();
    
        // Ejecutar funcionalidades
        IngredientType createdType = ingredientService.createIngredientTypeAsAdmin(DEFAULT_INGREDIENTTYPE_NAME, admin.getId());
        
        // Comprobar resultados
        assertAll(
            // Se crea el ingrediente una vez
            () -> assertNotNull(createdType),
            // No se puede crear dos veces
            () -> assertThrows(EntityAlreadyExistsException.class,
                () -> ingredientService.createIngredientTypeAsAdmin(DEFAULT_INGREDIENTTYPE_NAME, admin.getId())
            )
        );
    }
    
    @Test
    void whenCreateIngredientType_AndUserIsNotAdmin_thenPermissionException() {
        // Crear datos de prueba
        User fakeAdmin = generateValidUser();
        
        // Comprobar resultados
        assertThrows(PermissionException.class,
            () -> ingredientService.createIngredientTypeAsAdmin(DEFAULT_INGREDIENTTYPE_NAME, fakeAdmin.getId())
        );
    }
    
    
}
