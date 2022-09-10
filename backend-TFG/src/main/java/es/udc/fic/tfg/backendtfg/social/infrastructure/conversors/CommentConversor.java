package es.udc.fic.tfg.backendtfg.social.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.CommentDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentConversor {
    /* ******************** Convertir a DTO ******************** */
    public static CommentDTO toCommentDTO(Comment entity) {
        UserSummaryDTO author = UserConversor.toUserSummaryDTO(entity.getAuthor());
        
        return new CommentDTO(entity.getId(),
                              author,
                              entity.getCreationDate(),
                              entity.getText(),
                              entity.isBannedByAdmin()
        );
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static List<CommentDTO> toCommentDTOList(Set<Comment> entityList) {
        return entityList
                .stream()
                .map(CommentConversor::toCommentDTO)
                .collect(Collectors.toList());
    }
    
    public static BlockDTO<CommentDTO> toCommentBlockDTO(Block<Comment> block) {
        BlockDTO<CommentDTO> dto = new BlockDTO<>();
        List<CommentDTO> items = block.getItems()
                                      .stream()
                                      .map(CommentConversor::toCommentDTO)
                                      .collect(Collectors.toList());
        dto.setItems(items);
        dto.setHasMoreItems(block.hasMoreItems());
        dto.setItemsCount(block.getItemsCount());
        
        return dto;
    }
    
}
