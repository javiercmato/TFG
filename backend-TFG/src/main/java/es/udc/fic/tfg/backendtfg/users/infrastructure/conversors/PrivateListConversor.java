package es.udc.fic.tfg.backendtfg.users.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.PrivateListDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class PrivateListConversor {
    /* ******************** Convertir a DTO ******************** */
    public static PrivateListDTO toPrivateListDTO(PrivateList entity) {
        return new PrivateListDTO(entity.getId(), entity.getTitle(), entity.getDescription());
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
    
}
