package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParamsDTO {
    @NotBlank
    @Size(min = 1, max = 30)
    private String nickname;
    
    @NotBlank
    @Size(min = 1, max = 30)
    private String password;
}
