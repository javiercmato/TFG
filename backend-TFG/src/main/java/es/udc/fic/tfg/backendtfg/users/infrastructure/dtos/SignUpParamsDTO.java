package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpParamsDTO {
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
   
    @NotBlank
    @Size(min = 1, max = 30)
    private String nickname;
    
    @NotBlank
    @Size(min = 1, max = 30)
    private String password;
}
