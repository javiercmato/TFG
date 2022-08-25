package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateRecipeParamsDTO {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    
    private String description;
    
    @NotNull
    @Positive
    private long duration;
    
    @NotNull
    @Positive
    private int diners;
    
    @NotNull
    private UUID authorID;
    
    @NotNull
    private UUID categoryID;
    
    @NotNull
    private List<CreateRecipeStepParamsDTO> steps;
    
    private List<CreateRecipePictureParamsDTO> pictures;
    
    @NotNull
    private List<CreateRecipeIngredientParamsDTO> ingredients;
}
