package es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class IngredientSummaryDTO {
    @NotNull
    private UUID id;
    
    @NotBlank
    private String name;
}
