package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.MeasureUnit;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "recipes", name = "recipeingredient")
public class RecipeIngredient {
    @EmbeddedId
    private RecipeIngredientID id = new RecipeIngredientID();
    
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

}
