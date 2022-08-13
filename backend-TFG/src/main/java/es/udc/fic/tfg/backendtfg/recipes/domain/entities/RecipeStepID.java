package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/** Identificador de la entidad débil RecipeStep */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RecipeStepID implements Serializable {
    @Column(table = "recipestep", name = "recipe")
    private UUID recipeID;
    
    @Column(table = "recipestep", name = "step")
    private Integer step;
}
