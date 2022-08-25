package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CreateCategoryParamsDTO {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
