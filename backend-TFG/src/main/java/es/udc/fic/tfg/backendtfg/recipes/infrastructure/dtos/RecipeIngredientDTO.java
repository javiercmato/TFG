package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.IngredientSummaryDTO;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDTO {
    @NotNull
    private IngredientSummaryDTO ingredient;
    
    private String quantity;
    
    @NotNull
    private String measureUnit;
}
