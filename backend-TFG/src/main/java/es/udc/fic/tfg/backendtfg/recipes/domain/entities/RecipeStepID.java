package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/** Identificador de la entidad débil RecipeStep */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RecipeStepID implements Serializable {
    @Column(table = "recipestep", name = "recipe_id", nullable = false)
    private UUID recipeID;
    
    @Column(table = "recipestep", name = "step", nullable = false)
    private Integer step;
}
