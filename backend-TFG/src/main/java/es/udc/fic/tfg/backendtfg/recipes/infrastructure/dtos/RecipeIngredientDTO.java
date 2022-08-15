package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDTO {
    @NotNull
    private UUID id;
    
    private String name;
    
    private String quantity;
    
    @NotNull
    private String measureUnit;
}
