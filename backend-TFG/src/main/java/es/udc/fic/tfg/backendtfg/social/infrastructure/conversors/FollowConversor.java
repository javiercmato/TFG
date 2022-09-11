package es.udc.fic.tfg.backendtfg.social.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Follow;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.FollowDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowConversor {
    /* ******************** Convertir a DTO ******************** */
    public static FollowDTO toFollowDTO(Follow entity) {
        UserSummaryDTO followingDTO = UserConversor.toUserSummaryDTO(entity.getFollowing());
        UserSummaryDTO followedDTO = UserConversor.toUserSummaryDTO(entity.getFollowed());
        
        return new FollowDTO(followingDTO, followedDTO, entity.getFollowDate());
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static BlockDTO<FollowDTO> toFollowBlockDTO(Block<Follow> block) {
        BlockDTO<FollowDTO> dto = new BlockDTO<>();
        List<FollowDTO> items = block.getItems()
                                     .stream()
                                     .map(FollowConversor::toFollowDTO)
                                     .collect(Collectors.toList());
        dto.setItems(items);
        dto.setHasMoreItems(block.hasMoreItems());
        dto.setItemsCount(block.getItemsCount());
        
        return dto;
    }
}
