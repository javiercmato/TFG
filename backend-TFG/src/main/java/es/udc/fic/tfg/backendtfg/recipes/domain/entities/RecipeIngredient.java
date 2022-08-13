package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "recipes", name = "recipeingredient")
public class RecipeIngredient {
    @EmbeddedId
    private RecipeIngredientID id;
    
    @Column(name = "quantity")
    private String quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "measureunit", nullable = false)
    private MeasureUnit measureUnit;
    
    @ManyToOne
    @MapsId("recipeID")
    private Recipe recipe;
    
    @ManyToOne
    @MapsId("ingredientID")
    private Ingredient ingredient;
    
    
    /** Identificador de la relación N:M entre Recipe e Ingredient */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    static class RecipeIngredientID implements Serializable {
        @Column(table = "recipeingredient", name = "recipe")
        private UUID recipeID;
        
        @Column(table = "recipeingredient", name = "ingredient")
        private UUID ingredientID;
    }

}
