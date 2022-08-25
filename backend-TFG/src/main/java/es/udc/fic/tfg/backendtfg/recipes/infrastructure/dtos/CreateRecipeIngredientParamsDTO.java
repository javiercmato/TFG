package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeIngredientParamsDTO {
    @NotNull
    private UUID ingredientID;
    
    private String quantity;
    
    @NotNull
    private String measureUnit;
}
