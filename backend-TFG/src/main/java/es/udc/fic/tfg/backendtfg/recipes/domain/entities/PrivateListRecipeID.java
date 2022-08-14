package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/** Identificador de la relación N:M entre PrivateList y Recipe */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PrivateListRecipeID implements Serializable {
    @Column(table = "privatelistrecipe", name = "privatelist_id")
    private UUID privateListID;
    
    @Column(table = "privatelistrecipe", name = "recipe_id")
    private UUID recipeID;
}
