package es.udc.fic.tfg.backendtfg.users.application.services;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.users.application.utils.UserUtils;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectPasswordException;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtils userUtils;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    
    /* ******************** FUNCIONALIDADES USUARIO ******************** */
    @Override
    public User signUp(User user) throws EntityAlreadyExistsException {
        // Comprobar si existe un usuario con el mismo nickname
        if (userRepository.existsByNicknameIgnoreCase(user.getNickname()))
            throw new EntityAlreadyExistsException(User.class.getSimpleName(), user.getNickname());
        
        // Asignar datos por defecto del usuario
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegisterDate(LocalDateTime.now());
        user.setRole(UserRole.USER);
        
        // Guardar datos y devolver usuario creado
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    @Override
    public User login(String nickname, String rawPassword) throws IncorrectLoginException,
                                                                  ResourceBannedByAdministratorException {
        // Comprobar si existe el usuario recibido
        Optional<User> optionalUser = userRepository.findByNicknameIgnoreCase(nickname);
        if ( optionalUser.isEmpty() ) {
            throw new IncorrectLoginException();
        }
        User user = optionalUser.get();
        
        // Comprobar si usuario está baneado por administrador
        if (user.isBannedByAdmin())
            throw new ResourceBannedByAdministratorException();
    
        // Comprobar si las contraseñas coinciden
        if ( !passwordEncoder.matches(rawPassword, user.getPassword()) ) {
            throw new IncorrectLoginException();
        }
        
        return user;
    }
    
    @Transactional(readOnly = true)
    @Override
    public User loginFromToken(UUID userID) throws EntityNotFoundException, ResourceBannedByAdministratorException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        // Comprobar si usuario está baneado por administrador
        if (user.isBannedByAdmin())
            throw new ResourceBannedByAdministratorException();
        
        return user;
    }
    
    @Override
    public User updateProfile(UUID userID, String name, String surname, String email, byte[] avatar) throws EntityNotFoundException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        // Cambiar solo los datos que se han modificado
        if (name != null) user.setName(name);
        if (surname != null) user.setSurname(surname);
        if (email != null) user.setEmail(email);
        if (avatar != null) user.setAvatar(avatar);
        
        return userRepository.save(user);
    }
    
    @Override
    public void changePassword(UUID userID, String oldPassword, String newPassword) throws EntityNotFoundException, IncorrectPasswordException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        // Comprobar que contraseñas coincidan
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new IncorrectPasswordException();
        
        // Cambiar contraseña y actualizar usuario
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        
        userRepository.save(user);
    }
    
    @Override
    public void deleteUser(UUID userID) throws EntityNotFoundException {
        // Obtener al usuario
        User user = userUtils.fetchUserByID(userID);
        
        // Eliminar al usuario
        userRepository.delete(user);
    }
    
    @Transactional(readOnly = true)
    @Override
    public User findUserById(UUID userID) throws EntityNotFoundException {
        return userUtils.fetchUserByID(userID);
    }
    
    @Transactional(readOnly = true)
    @Override
    public User findUserByNickname(String nickname) throws EntityNotFoundException {
        return userUtils.fetchUserByNickname(nickname);
    }
    
    
    
    /* ******************** FUNCIONALIDADES ADMINISTRADOR ******************** */
    @Override
    public boolean banUserAsAdmin(UUID adminID, UUID targetUserID) throws EntityNotFoundException {
        // Commprobar si existe el administrador
        userUtils.fetchAdministrator(adminID);
        
        // Obtener al usuario a banear
        User targetUser = userUtils.fetchUserByID(targetUserID);
        
        // Aplicar/retirar baneo
        targetUser.setBannedByAdmin(!targetUser.isBannedByAdmin());
        userRepository.save(targetUser);
        
        return targetUser.isBannedByAdmin();
    }
}
