package es.udc.fic.tfg.backendtfg.users.application.services;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityAlreadyExistsException;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectPasswordException;

import java.util.UUID;

public interface UserService {
    /**
     * Registra un nuevo usuario.
     * @param user Datos del usuario a registrar
     * @return Usuario registrado
     * @throws EntityAlreadyExistsException Usuario ya existe
     */
    User signUp(User user) throws EntityAlreadyExistsException;
    
    /**
     * Iniciar sesión en el sistema.
     * @param nickname Nombre de usuario
     * @param rawPassword Contraseña (sin cifrar)
     * @return Datos del usuario autenticado
     * @throws IncorrectLoginException Nickname o contraseña incorrectos
     */
    User login(String nickname, String rawPassword) throws IncorrectLoginException;
    
    /**
     * Iniciar sesión en el sistema con un token (JWT).
     * @param userID ID del usuario
     * @return Datos del usuario autenticado
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    User loginFromToken(UUID userID) throws EntityNotFoundException;
    
    /**
     * Actualizar datos del usuario.
     * @param userID ID del usuario (para buscarlo en el sistema)
     * @param name Nuevo nombre de pila del usuario
     * @param surname Nuevo apellido del usuario
     * @param email Nuevo email del usuario
     * @param avatar Nueva imagen de perfil del usuario
     * @return Datos del usuario actualizados
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    User updateProfile(UUID userID, String name, String surname, String email, byte[] avatar) throws EntityNotFoundException;
    
    /**
     * Cambiar contraseña del usuario.
     * @param userID ID del usuario
     * @param oldPassword Antigua contraseña
     * @param newPassword Nueva contraseña
     * @throws EntityNotFoundException No se encuentra al usuario
     * @throws IncorrectPasswordException Contraseñas no coinciden
     */
    void changePassword(UUID userID, String oldPassword, String newPassword) throws EntityNotFoundException, IncorrectPasswordException;
    
    /**
     * Eliminar perfil de usuario.
     * @param userID ID del usuario
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    void deleteUser(UUID userID) throws EntityNotFoundException;
    
    /**
     * Buscar un usuario por su ID.
     * @param userID ID del usuario
     * @return Datos del usuario
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    User findUserById(UUID userID) throws EntityNotFoundException;
    
    /**
     * Buscar un usuario por su nombre de usuario.
     * @param nickname Nombre del usuario
     * @return Datos del usuario
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    User findUserByNickname(String nickname) throws EntityNotFoundException;
    
    /**
     * Banea a un usuario del sistema.
     * Si el usuario estaba baneado previamente, lo desbanea.
     * Caso de uso restringido al administrador.
     * @param adminID ID del administrador
     * @param targetUserID ID del usuario a banear
     * @return True si el usuario está baneado; false si no está baneado.
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    boolean banUserAsAdmin(UUID adminID, UUID targetUserID) throws EntityNotFoundException;
}
