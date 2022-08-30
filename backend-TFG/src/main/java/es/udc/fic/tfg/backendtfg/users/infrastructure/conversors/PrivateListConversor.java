package es.udc.fic.tfg.backendtfg.users.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.PrivateListDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.PrivateListSummaryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class PrivateListConversor {
    /* ******************** Convertir a DTO ******************** */
    public static PrivateListDTO toPrivateListDTO(PrivateList entity) {
        PrivateListDTO dto = new PrivateListDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        
        return dto;
    }
    
    public static PrivateListSummaryDTO toPrivateListSummaryDTO(PrivateList entity) {
        return new PrivateListSummaryDTO(entity.getId(), entity.getTitle());
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static List<PrivateListSummaryDTO> toPrivateListSummaryDTOList(List<PrivateList> entitiesList) {
        return entitiesList.stream()
                           .map(item -> toPrivateListSummaryDTO(item))
                           .collect(Collectors.toList());
    }
    
}
