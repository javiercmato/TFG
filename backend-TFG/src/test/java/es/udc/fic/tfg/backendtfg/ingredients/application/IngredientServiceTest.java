package es.udc.fic.tfg.backendtfg.ingredients.application;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.PNG_EXTENSION;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.loadImageFromResourceName;
import static es.udc.fic.tfg.backendtfg.utils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class IngredientServiceTest {
    private final int INITIAL_PAGE = 0;
    private final int PAGE_SIZE = 10;
    
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
    
    @Test
    void whenFindAllIngredientTypes_ThenAllTypesAreFound()
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        int ITEMS_AMMOUNT = 10;
        User admin = findAdmin();
        List<IngredientType> expectedTypes = new ArrayList<>(ITEMS_AMMOUNT);
        for (int i=0; i < ITEMS_AMMOUNT; i++) {
            IngredientType type = ingredientService.createIngredientTypeAsAdmin(DEFAULT_INGREDIENTTYPE_NAME + i, admin.getId());
            expectedTypes.add(type);
        }
        
        // Ejecutar funcionalidades
        List<IngredientType> types = ingredientService.getIngredientTypes();
        
        // Comprobar resultados
        assertAll(
            // Se han obtenido resultados
            () -> assertNotNull(types),
            // Hay exactamente la cantidad de resultados esperada
            () -> assertEquals(ITEMS_AMMOUNT, types.size()),
            // Los elementos recibidos son los esperados
            () -> assertEquals(expectedTypes, types)
        );
    }
    
    @Test
    void whenFindAllIngredients_ThenIngredientsAreFound()
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        int INGREDIENTS_AMMOUNT = PAGE_SIZE;
        User admin = findAdmin();
        User creator = generateValidUser();
        IngredientType type = ingredientService.createIngredientTypeAsAdmin(DEFAULT_INGREDIENTTYPE_NAME, admin.getId());
        // Crear los ingredientes
        List<Ingredient> expectedIngredients = new ArrayList<>();
        for ( int i=0; i < INGREDIENTS_AMMOUNT; i++ ) {
            // Mete los ingredientes en un tipo u otro según el índice actual
            String name = String.format(DEFAULT_INGREDIENT_NAME + "%02d", i);       // Dar formato al nombre para evitar problemas del estilo Ing1 > Ing11
            Ingredient ingredient = ingredientService.createIngredient(name, type.getId(), creator.getId());
            expectedIngredients.add(ingredient);
        }
        
        // Ejecutar funcionalidades
        Block<Ingredient> ingredientBlock = ingredientService.findAllIngredients(INITIAL_PAGE, PAGE_SIZE);
        
        // Comprobar resultados
        assertAll(
                // Se han obtenido resultados
                () -> assertNotNull(ingredientBlock),
                // Hay exactamente la cantidad de resultados esperada
                () -> assertEquals(expectedIngredients.size(), ingredientBlock.getItemsCount()),
                // Los elementos recibidos son los esperados
                () -> assertEquals(expectedIngredients, ingredientBlock.getItems()),
                // No hay más elementos por cargar
                () -> assertFalse(ingredientBlock.hasMoreItems())
        );
    }
    
    @Test
    void whenFindIngredientsByName_ThenIngredientsAreFound()
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        User admin = findAdmin();
        User creator = generateValidUser();
        IngredientType type = ingredientService.createIngredientTypeAsAdmin(DEFAULT_INGREDIENTTYPE_NAME, admin.getId());
        // Crear los ingredientes
        Ingredient panMolde = ingredientService.createIngredient("Pan de molde", type.getId(), creator.getId());
        Ingredient panBarra = ingredientService.createIngredient("Pan de barra", type.getId(), creator.getId());
        Ingredient platano = ingredientService.createIngredient("Plátano", type.getId(), creator.getId());
        Ingredient empanada = ingredientService.createIngredient("Empanada", type.getId(), creator.getId());
        // Lista de ingredientes esperada
        List<Ingredient> expectedIngredients = new ArrayList<>();
        expectedIngredients.add(empanada);
        expectedIngredients.add(panBarra);
        expectedIngredients.add(panMolde);
        
        // Ejecutar funcionalidades
        String keyword = "pan";
        Block<Ingredient> ingredientBlock = ingredientService.findIngredientsByName(keyword, INITIAL_PAGE, PAGE_SIZE);
        
        // Comprobar resultados
        assertAll(
                // Se han obtenido resultados
                () -> assertNotNull(ingredientBlock),
                // Hay exactamente la cantidad de resultados esperada (buscamos los elementos de evenType)
                () -> assertEquals(expectedIngredients.size(), ingredientBlock.getItemsCount()),
                // Los elementos recibidos son los esperados
                () -> assertEquals(expectedIngredients, ingredientBlock.getItems()),
                // No hay más elementos por cargar
                () -> assertFalse(ingredientBlock.hasMoreItems())
        );
    }
    
    @Test
    void whenFindIngredientsByType_ThenIngredientsAreFound()
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        int ITEMS_AMMOUNT = 2*PAGE_SIZE;
        User admin = findAdmin();
        User creator = generateValidUser();
        IngredientType evenType = ingredientService.createIngredientTypeAsAdmin("Even Type", admin.getId());
        IngredientType oddType = ingredientService.createIngredientTypeAsAdmin("Odd Type", admin.getId());
        List<Ingredient> createdIngredients = new ArrayList<>(PAGE_SIZE);
        for (int i=0; i < ITEMS_AMMOUNT; i++) {
            // Mete los ingredientes en un tipo u otro según el índice actual
            IngredientType type = ((i % 2) == 0) ? evenType : oddType;
            String name = String.format(DEFAULT_INGREDIENT_NAME + "%02d", i);       // Dar formato al nombre para evitar problemas del estilo Ing1 > Ing11
            Ingredient ingredient = ingredientService.createIngredient(name, type.getId(), creator.getId());
            createdIngredients.add(ingredient);
        }
        // Lista de ingredientes esperada: evenType (pares)
        List<Ingredient> expectedIngredients = createdIngredients
                .stream()
                .filter((ingredient -> ingredient.getIngredientType().equals(evenType)))
                .collect(Collectors.toList());
        
        // Ejecutar funcionalidades
        Block<Ingredient> ingredientBlock = ingredientService.findIngredientsByType(evenType.getId(), INITIAL_PAGE, PAGE_SIZE);
        
        // Comprobar resultados
        assertAll(
                // Se han obtenido resultados
                () -> assertNotNull(ingredientBlock),
                // Hay exactamente la cantidad de resultados esperada (buscamos los elementos de evenType)
                () -> assertEquals(PAGE_SIZE, ingredientBlock.getItemsCount()),
                // Los elementos recibidos son los esperados
                () -> assertEquals(expectedIngredients, ingredientBlock.getItems()),
                // Hay más elementos por cargar
                () -> assertFalse(ingredientBlock.hasMoreItems())
        );
    }
    
    
}
