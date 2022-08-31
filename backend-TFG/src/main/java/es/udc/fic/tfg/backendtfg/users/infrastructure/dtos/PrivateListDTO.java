package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.RecipeSummaryDTO;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PrivateListDTO {
    @NotNull
    private UUID id;
    
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    
    @Size(max = 100)
    private String description;
    
    @NotNull
    private List<RecipeSummaryDTO> recipes;
}
