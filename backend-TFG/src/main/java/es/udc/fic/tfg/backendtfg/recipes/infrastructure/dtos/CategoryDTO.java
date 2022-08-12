package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CategoryDTO {
    @NotNull
    private UUID id;
    
    @NotBlank
    private String name;
}
