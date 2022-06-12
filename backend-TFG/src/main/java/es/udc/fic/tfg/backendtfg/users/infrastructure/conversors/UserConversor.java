package es.udc.fic.tfg.backendtfg.users.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.common.infrastructure.conversors.ImageConversor;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.entities.UserRole;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

@UtilityClass
public class UserConversor {
    @Autowired
    private ImageConversor imageConversor;
    
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
            String base64EncodedAvatar = imageConversor.encodeToBase64String(entity.getAvatar());
            dto.setAvatar(base64EncodedAvatar);
        }
        
        return dto;
    }
    
    public static AuthenticatedUserDTO toAuthenticatedUserDTO(User entity, String token) {
        AuthenticatedUserDTO dto = new AuthenticatedUserDTO();
        dto.setUserDTO(toUserDTO(entity));
        dto.setServiceToken(token);
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
    
    /* ******************** Convertir a Entidad ******************** */
    public static User fromUserDTO(UserDTO dto) {
        User entity = new User();
        entity.setId(dto.getUserID());
        entity.setNickname(dto.getNickname());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setRegisterDate(dto.getRegisterDate());
        entity.setRole(UserRole.valueOf(dto.getRole()));
        entity.setBannedByAdmin(dto.isBannedByAdmin());
        
        // Decodificar la imagen
        byte[] byteArrayImage = imageConversor.encodeToByteArray(dto.getAvatar());
        entity.setAvatar(byteArrayImage);
        
        return entity;
    }
    
    public static User fromSignUpParamsDTO(SignUpParamsDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setNickname(dto.getNickname());
        entity.setPassword(dto.getPassword());
        
        // Decodificar la imagen (si existe)
        if (dto.getAvatar() != null) {
            byte[] byteArrayImage = imageConversor.encodeToByteArray(dto.getAvatar());
            entity.setAvatar(byteArrayImage);
        }
        
        return entity;
    }
    
    public static User fromUpdateProfileParamsDTO(UpdateProfileParamsDTO dto) {
        User entity = new User();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        
        // Decodificar la imagen
        byte[] byteArrayImage = imageConversor.encodeToByteArray(dto.getAvatar());
        entity.setAvatar(byteArrayImage);
        
        return entity;
    }
    
    
}
