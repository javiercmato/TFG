package es.udc.fic.tfg.backendtfg.users.application.services;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.PNG_EXTENSION;
import static es.udc.fic.tfg.backendtfg.utils.ImageUtils.loadImageFromResourceName;
import static es.udc.fic.tfg.backendtfg.utils.UserTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    
    /* ************************* MÉTODOS AUXILIARES ************************* */
    
    /** Genera datos de un usuario válido. */
    private User generateValidUser(String nickname) {
        User user = new User();
        user.setNickname(nickname);
        user.setPassword(DEFAULT_PASSWORD);
        user.setName(DEFAULT_NAME);
        user.setSurname(DEFAULT_SURNAME);
        user.setEmail(nickname + DEFAULT_EMAIL_DOMAIN);
        user.setRole(UserRole.USER);
        user.setBannedByAdmin(false);
        user.setRegisterDate(LocalDateTime.now());
        user.setAvatar(loadImageFromResourceName(DEFAULT_AVATAR_NAME, PNG_EXTENSION));
        
        return user;
    }
    
    
    /* ************************* CASOS DE PRUEBA ************************* */
    @Test
    void whenSigningUp_thenNewUserIsCreated( ) throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(validUser);
        
        // Comprobar resultados
        User databaseUser = userRepository.findByNicknameIgnoreCase(nickname).get();
        assertEquals(registeredUser, databaseUser);
    }
    
    
    @Test
    void whenSigningUpTwice_thenUserAlreadyExists( ) throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        
        // Ejecutar funcionalidades
        User user = userService.signUp(expectedUser);
        
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(user),
                // Se lanza excepción al volver a registrarlo
                ( ) -> assertThrows(
                        EntityAlreadyExistsException.class,
                        ( ) -> userService.signUp(expectedUser)
                )
        );
    }
    
    
    @Test
    void whenLogin_thenUserDataIsReturned()
            throws EntityAlreadyExistsException, IncorrectLoginException, ResourceBannedByAdministratorException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String clearPassword = expectedUser.getPassword();
        
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        User loggedUser = userService.login(nickname, clearPassword);
        
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Se puede recuperar los datos del usuario
                () -> assertEquals(expectedUser.getNickname(), loggedUser.getNickname()),
                () -> assertNotNull(expectedUser.getAvatar()),
                () -> assertEquals(UserRole.USER, expectedUser.getRole())
        );
    }
    
    
    @Test
    void whenLoginWithIncorrectNickname_thenRaiseIncorretLoginException() throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String clearPassword = expectedUser.getPassword();
        
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Se lanza la excepción
                () -> assertThrows(
                        IncorrectLoginException.class,
                        () -> userService.login(nickname + 'X', clearPassword)
                )
        );
    }
    
    
    @Test
    void whenLoginWithIncorrectPassword_thenRaiseIncorretLoginException() throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String clearPassword = expectedUser.getPassword();
    
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Se lanza la excepción
                () -> assertThrows(
                        IncorrectLoginException.class,
                        () -> userService.login(nickname, clearPassword + 'X')
                )
        );
    }
    
    
    @Test
    void whenLoginNonExistentUser_thenRaiseIncorrectLoginException() {
        // Comprobar resultados
        assertAll(
                // Se lanza la excepción
                () -> assertThrows(
                        IncorrectLoginException.class,
                        () -> userService.login(NON_EXISTENT_NICKNAME, DEFAULT_PASSWORD)
                )
        );
    }
    
    
    @Test
    void whenUserIsBanned_andLogin_thenRaiseResourceBannedByAdminException() throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String clearPassword = expectedUser.getPassword();
        expectedUser.setBannedByAdmin(true);
    
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
    
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Usuario está baneado
                () -> assertTrue(registeredUser.isBannedByAdmin()),
                // Se lanza la excepción
                () -> assertThrows(
                        ResourceBannedByAdministratorException.class,
                        () -> userService.login(nickname, clearPassword)
                )
        );
    }
    
    
    @Test
    void whenLoginWithID_thenUserDataIsReturned()
            throws EntityAlreadyExistsException, ResourceBannedByAdministratorException, EntityNotFoundException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String clearPassword = expectedUser.getPassword();
    
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        User loggedUser = userService.loginFromToken(registeredUser.getId());
    
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Se puede recuperar los datos del usuario
                () -> assertEquals(expectedUser.getNickname(), loggedUser.getNickname()),
                () -> assertNotNull(expectedUser.getAvatar()),
                () -> assertEquals(UserRole.USER, expectedUser.getRole())
        );
    }
    
    
    @Test
    void whenLoginWithIDNonExistentUser_thenRaiseEntityNotFoundException() {
        // Comprobar resultados
        assertAll(
                // Se lanza la excepción
                () -> assertThrows(
                        EntityNotFoundException.class,
                        () -> userService.loginFromToken(NON_EXISTENT_UUID)
                )
        );
    }
    
    @Test
    void whenUserIsBanned_andLoginWithID_thenRaiseResourceBannedByAdminException() throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String clearPassword = expectedUser.getPassword();
        expectedUser.setBannedByAdmin(true);
        
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Usuario está baneado
                () -> assertTrue(registeredUser.isBannedByAdmin()),
                // Se lanza la excepción
                () -> assertThrows(
                        ResourceBannedByAdministratorException.class,
                        () -> userService.loginFromToken(registeredUser.getId())
                )
        );
    }
    
    
}
