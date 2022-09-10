package es.udc.fic.tfg.backendtfg.social.domain.entities;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating", schema = "social")
public class Rating {
    @EmbeddedId
    private RatingID id = new RatingID();
    
    @NotNull
    @PositiveOrZero
    @Column(name = "rating", nullable = false)
    private Integer value;
    
    @ManyToOne
    @MapsId("recipeID")
    private Recipe recipe;
    
    @ManyToOne
    @MapsId("authorID")
    private User author;
    
    
    /* *************** Domain-Model *************** */
    
}
