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
public class RatingID implements Serializable {
    @Column(table = "rating", name = "author_id", nullable = false)
    private UUID authorID;
    
    @Column(table = "rating", name = "recipe_id", nullable = false)
    private UUID recipeID;
}
