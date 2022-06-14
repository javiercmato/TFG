package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserDTO {
    @JsonProperty(value = "serviceToken")
    private String serviceToken;
    
    @JsonProperty(value = "user")
    private UserDTO userDTO;
}
