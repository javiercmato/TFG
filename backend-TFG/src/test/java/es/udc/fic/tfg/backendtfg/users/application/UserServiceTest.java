package es.udc.fic.tfg.backendtfg.users.application;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.users.application.services.UserService;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectPasswordException;
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
        user.setPrivateLists(Collections.emptySet());
        
        return user;
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
        assertAll(
            // Usuario se ha registrado
            () -> assertNotNull(registeredUser),
            // Datos son los mismos
            () -> assertEquals(registeredUser, databaseUser),
            // Usuario nuevo no tiene listas privadas
            () -> assertTrue(registeredUser.getPrivateLists().isEmpty())
            
        );
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
    void whenLogin_thenUserDataIsReturned() throws EntityAlreadyExistsException, IncorrectLoginException {
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
    void whenLoginWithID_thenUserDataIsReturned() throws EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
    
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
    void whenChangePassword_thenPasswordIsChanged()
            throws EntityAlreadyExistsException, IncorrectPasswordException, EntityNotFoundException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String oldPassword = expectedUser.getPassword();
        String newPassword = oldPassword + 'X';
        
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        userService.changePassword(registeredUser.getId(), oldPassword, newPassword);
        
        // Comprobar resultados
        assertAll(
                // Puede iniciar sesión con la nueva contraseña
                () -> assertDoesNotThrow(
                        () -> userService.login(nickname, newPassword)
                ),
                // No puede iniciar sesión con la contraseña antigua
                () -> assertThrows(
                        IncorrectLoginException.class,
                        () -> userService.login(nickname, oldPassword)
                )
        );
    }
    
    
    @Test
    void whenChangePasswordNonExistentUser_thenEntityNotFoundException() {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String oldPassword = expectedUser.getPassword();
        String newPassword = oldPassword + 'X';
        
        // Comprobar resultados
        assertAll(
                // No se encuentra al usuario
                () -> assertThrows(
                        EntityNotFoundException.class,
                        () -> userService.changePassword(NON_EXISTENT_UUID, DEFAULT_PASSWORD, newPassword)
                )
        );
    }
    
    @Test
    void whenChangePassword_andNotMatching_thenIncorrectPasswordException() throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        String oldPassword = expectedUser.getPassword() + "1234";
        String newPassword = oldPassword + 'X';
    
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        
        // Comprobar resultados
        assertAll(
                // No se encuentra al usuario
                () -> assertThrows(
                        IncorrectPasswordException.class,
                        () -> userService.changePassword(registeredUser.getId(), oldPassword, newPassword)
                )
        );
    }
    
    
    @Test
    void whenUpdateProfile_thenProfileIsUpdated() throws EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        String nickname = "Foo";
        User originalUser = userService.signUp(generateValidUser(nickname));
        UUID userID = originalUser.getId();
        
        // Ejecutar funcionalidades
        String updatedName = originalUser.getName() + "TEST";
        String updatedSurname = originalUser.getSurname() + "TEST";
        String updatedEmail = "TEST" + originalUser.getEmail();
        byte[] updatedAvatar = loadImageFromResourceName("updated_user_avatar.png", PNG_EXTENSION);
        
        // Comprobar resultdos
        User updatedUser = userService.updateProfile(userID, updatedName, updatedSurname, updatedEmail, updatedAvatar);
        assertAll(
                () -> assertEquals(userID, updatedUser.getId()),
                // Se han cambiado los atributos
                () -> assertEquals(updatedName, updatedUser.getName()),
                () -> assertEquals(updatedSurname, updatedUser.getSurname()),
                () -> assertEquals(updatedEmail, updatedUser.getEmail()),
                () -> assertEquals(updatedAvatar, updatedUser.getAvatar())
        );
    }
    
    @Test
    void whenDeleteUser_thenUserDontExist() throws EntityAlreadyExistsException, EntityNotFoundException {
        // Crear datos de prueba
        String nickname = "Foo";
        User user = generateValidUser(nickname);
        
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(user);
        UUID userID = registeredUser.getId();
        userService.deleteUser(userID);
        
        // Comprobar resultados
        assertAll(
                // Ha existido un usuario
                () -> assertNotNull(registeredUser),
                // Se ha borrado el usuario
                () -> assertTrue(userRepository.findById(userID).isEmpty())
        );
    }
    
    @Test
    void whenDeleteNonExistentUser_thenEntityNotFound() {
        assertThrows(
            EntityNotFoundException.class,
            () -> userService.deleteUser(NON_EXISTENT_UUID));
    }
    
    
    @Test
    void whenFindUserByID_thenUserIsFound() throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
    
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        UUID userID = registeredUser.getId();
    
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Se puede recuperar al usuario por su id
                () -> assertEquals(registeredUser, userService.findUserById(userID))
        );
    }
    
    
    @Test
    void whenFindUserByNickname_thenUserIsFound() throws EntityAlreadyExistsException {
        // Crear datos de prueba
        String nickname = "Foo";
        User expectedUser = generateValidUser(nickname);
        
        // Ejecutar funcionalidades
        User registeredUser = userService.signUp(expectedUser);
        
        // Comprobar resultados
        assertAll(
                // Se ha registrado un usuario
                () -> assertNotNull(registeredUser),
                // Se puede recuperar al usuario por su id
                () -> assertEquals(registeredUser, userService.findUserByNickname(nickname))
        );
    }
    
    
    @Test
    void whenAdminBansUsers_thenUserIsBanned() throws EntityAlreadyExistsException, EntityNotFoundException, PermissionException {
        // Crear datos de prueba
        String nickname = "Foo";
        User targetUser = userService.signUp(generateValidUser(nickname));
        User admin = findAdmin();
        
        // Ejecutar funcionalidades
        boolean isBanned = userService.banUserAsAdmin(admin.getId(), targetUser.getId());
        
        // Comprobar resultados
        assertAll(
                // Se encuentra al administrador
                () -> assertNotNull(admin),
                // Usuario ahora está baneado
                () -> assertTrue(isBanned)
        );
    }
    
    
    @Test
    void whenAdminBansUsers_andUserWasBanned_thenUserIsNotBannedAnymore()
            throws EntityAlreadyExistsException, EntityNotFoundException, PermissionException {
        // Crear datos de prueba
        String nickname = "Foo";
        User validUser = generateValidUser(nickname);
        User targetUser = userService.signUp(validUser);
        targetUser.setBannedByAdmin(true);
        userRepository.save(targetUser);
        User admin = findAdmin();
        
        // Ejecutar funcionalidades
        boolean isBanned = userService.banUserAsAdmin(admin.getId(), targetUser.getId());
        
        // Comprobar resultados
        assertAll(
                // Se encuentra al administrador
                () -> assertNotNull(admin),
                // Usuario ahora ya no está baneado
                () -> assertFalse(isBanned)
        );
    }
}
