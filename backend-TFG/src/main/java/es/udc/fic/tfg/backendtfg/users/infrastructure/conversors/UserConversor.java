package es.udc.fic.tfg.backendtfg.users.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConversor {
    /* ******************** Convertir a DTO ******************** */
    public static UserDTO toUserDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setUserID(entity.getId());
        dto.setNickname(entity.getNickname());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setRegisterDate(entity.getRegisterDate());
        dto.setRole(entity.getRole().toString());
        dto.setBannedByAdmin(entity.isBannedByAdmin());
        
        // Codificar la imagen (si existe)
        if (entity.getAvatar() != null) {
            dto.setAvatar(
                Base64.getEncoder().encodeToString(entity.getAvatar())
            );
        }
        
        return dto;
    }
    
    public static AuthenticatedUserDTO toAuthenticatedUserDTO(User entity, String token) {
        return new AuthenticatedUserDTO(token, toUserDTO(entity));
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
    
    /* ******************** Convertir a Entidad ******************** */
    public static User fromSignUpParamsDTO(SignUpParamsDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setNickname(dto.getNickname());
        entity.setPassword(dto.getPassword());
        
        // Decodificar la imagen (si existe)
        if (dto.getAvatar() != null) {
            entity.setAvatar(
                Base64.getDecoder().decode(dto.getAvatar())
            );
        }
        
        return entity;
    }
    
    public static User fromUpdateProfileParamsDTO(UpdateProfileParamsDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        
        // Decodificar la imagen (si existe)
        if (dto.getAvatar() != null) {
            entity.setAvatar(
                    Base64.getDecoder().decode(dto.getAvatar())
            );
        }
        
        return entity;
    }
    
    
}
