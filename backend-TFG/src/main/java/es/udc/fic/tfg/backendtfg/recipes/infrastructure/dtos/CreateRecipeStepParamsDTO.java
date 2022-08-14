package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeStepParamsDTO {
    @NotNull
    @Positive
    private int step;
    
    @NotBlank
    private String text;
}
