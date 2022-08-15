package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStepDTO {
    @NotNull
    @Positive
    private Integer step;
    
    @NotNull
    private String text;
}
