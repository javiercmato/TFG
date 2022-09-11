package es.udc.fic.tfg.backendtfg.social.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Follow;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.FollowDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FollowConversor {
    /* ******************** Convertir a DTO ******************** */
    public static FollowDTO toFollowDTO(Follow entity) {
        UserSummaryDTO followingDTO = UserConversor.toUserSummaryDTO(entity.getFollowing());
        UserSummaryDTO followedDTO = UserConversor.toUserSummaryDTO(entity.getFollowed());
        
        return new FollowDTO(followingDTO, followedDTO, entity.getFollowDate());
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
}
