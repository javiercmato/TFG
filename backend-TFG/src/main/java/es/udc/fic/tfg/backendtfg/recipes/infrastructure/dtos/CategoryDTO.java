package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    @NotNull
    private UUID id;
    
    @NotBlank
    private String name;
}
