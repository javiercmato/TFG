package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrivateListParamsDTO {
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    
    @Size(max = 100)
    private String description;
}
