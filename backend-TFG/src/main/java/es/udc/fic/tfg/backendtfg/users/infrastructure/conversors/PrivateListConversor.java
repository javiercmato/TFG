package es.udc.fic.tfg.backendtfg.users.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.PrivateListDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class PrivateListConversor {
    /* ******************** Convertir a DTO ******************** */
    public static PrivateListDTO toPrivateListDTO(PrivateList entity) {
        PrivateListDTO dto = new PrivateListDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        
        // Datos del usuario
        User creator = entity.getCreator();
        UserSummaryDTO userSummaryDTO = new UserSummaryDTO(creator.getId(), creator.getName(), creator.getNickname());
        dto.setUserSummaryDTO(userSummaryDTO);
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
    
}
