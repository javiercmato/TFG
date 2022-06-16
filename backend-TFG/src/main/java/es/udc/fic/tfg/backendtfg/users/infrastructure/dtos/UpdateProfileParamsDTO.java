package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class UpdateProfileParamsDTO {
    @NotBlank
    @Size(min = 1, max = 30)
    private String name;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String surname;
    
    @NotBlank
    @Email
    @Size(min = 1, max = 100)
    private String email;
    
    private String avatar;
}
