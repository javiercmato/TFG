package es.udc.fic.tfg.backendtfg.social.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CommentID implements Serializable {
    @Column(table = "comment", name = "author_id")
    private UUID authorID;
    
    @Column(table = "comment", name = "recipe_id")
    private UUID recipeID;
}
