package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/** Identificador de la relación N:M entre Recipe e Ingredient */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RecipeIngredientID implements Serializable {
    @Column(table = "recipeingredient", name = "recipe_id")
    private UUID recipeID;
    
    @Column(table = "recipeingredient", name = "ingredient_id")
    private UUID ingredientID;
}
