package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/** Identificador de la entidad débil RecipePicture */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RecipePictureID implements Serializable {
    @Column(table = "recipepicture", name = "recipe_id")
    private UUID recipeID;
    
    @Column(table = "recipepicture", name = "pictureorder")
    private Integer order;
}
