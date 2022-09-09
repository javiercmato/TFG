package es.udc.fic.tfg.backendtfg.social.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.CommentDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentConversor {
    /* ******************** Convertir a DTO ******************** */
    public static CommentDTO toCommentDTO(Comment entity) {
        UserSummaryDTO author = UserConversor.toUserSummaryDTO(entity.getAuthor());
        
        return new CommentDTO(author, entity.getCreationDate(), entity.getText());
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
    
}
