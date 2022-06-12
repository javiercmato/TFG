package es.udc.fic.tfg.backendtfg.users.application.utils;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.users.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class UserUtils {
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Busca al administrador
     * @param adminID ID del administrador
     * @return Administrador
     * @throws EntityNotFoundException No se encuentra al administrador
     */
    public User fetchAdministrator(UUID adminID) throws EntityNotFoundException {
        User admin = this.fetchUserByID(adminID);
        if (!admin.getRole().equals(UserRole.ADMIN))
            throw new EntityNotFoundException("administrator", adminID);
        
        return admin;
    }
    
    /**
     * Busca un usuario por el ID en la base de datos.
     * @param userID ID del usuario a buscar
     * @return Usuario encontrado
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    public User fetchUserByID(UUID userID) throws EntityNotFoundException {
        // Comprobar si existe el usuario con el ID recibido
        Optional<User> optionalUser = userRepository.findById(userID);
        if ( optionalUser.isEmpty() )
            throw new EntityNotFoundException(User.class.getName(), userID);
        
        return optionalUser.get();
    }
    
    /**
     * Busca un usuario por su nickname en la base de datos.
     * @param nickname Nombre de usuario a buscar
     * @return Usuario encontrado
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    public User fetchUserByNickname(String nickname) throws EntityNotFoundException {
        // Comprobar si existe el usuario con el ID recibido
        Optional<User> optionalUser = userRepository.findByNicknameIgnoreCase(nickname);
        if ( optionalUser.isEmpty() )
            throw new EntityNotFoundException(User.class.getName(), nickname);
        
        return optionalUser.get();
    }
}
