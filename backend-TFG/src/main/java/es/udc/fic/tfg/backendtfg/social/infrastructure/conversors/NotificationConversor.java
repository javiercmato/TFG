package es.udc.fic.tfg.backendtfg.social.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Notification;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.NotificationDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationConversor {
    /* ******************** Convertir a DTO ******************** */
    public static NotificationDTO toNotificationDTO(Notification entity) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setRead(entity.isRead());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setCreatedAt(entity.getCreatedAt());
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static BlockDTO<NotificationDTO> toNotificationBlockDTO(Block<Notification> block) {
        BlockDTO<NotificationDTO> dto = new BlockDTO<>();
        List<NotificationDTO> items = block.getItems()
                                     .stream()
                                     .map(NotificationConversor::toNotificationDTO)
                                     .collect(Collectors.toList());
        dto.setItems(items);
        dto.setHasMoreItems(block.hasMoreItems());
        dto.setItemsCount(block.getItemsCount());
        
        return dto;
    }
}
