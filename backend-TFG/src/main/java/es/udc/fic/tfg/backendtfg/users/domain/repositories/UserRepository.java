package es.udc.fic.tfg.backendtfg.users.domain.repositories;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    /**
     * Comprueba si existe un usuario por su nombre de usuario, ignorando maýusculas o minúsculas.
     * @param nickname Nombre del usuario
     * @return True si existe un usuario con el nombre recibido.
     */
    boolean existsByNicknameIgnoreCase(String nickname);
    
    Optional<User> findByNicknameIgnoreCase(String nickname);
}
