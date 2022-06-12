package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    /**
     * Atributos que debe contener obligatoriamente
     */
    public interface AllValidations {}
    
    /**
     * Atributos que debe contener al actualizar datos del usuario
     */
    public interface UpdateValidations {}
    
    
    private UUID userID;
    
    @NotBlank(groups = {AllValidations.class})
    @Size(min = 1, max = 30, groups = {AllValidations.class})
    private String nickname;
    
    @NotBlank(groups = {AllValidations.class})
    @Size(min = 1, max = 30, groups = {AllValidations.class, UpdateValidations.class})
    private String name;
    
    @NotNull(groups = {AllValidations.class, UpdateValidations.class})
    @Size(min = 1, max = 50, groups = {AllValidations.class, UpdateValidations.class})
    private String surname;
    
    @NotBlank(groups = {AllValidations.class})
    @Size(min = 1, max = 100, groups = {AllValidations.class, UpdateValidations.class})
    @Email(groups = {AllValidations.class, UpdateValidations.class, UpdateValidations.class})
    private String email;
    
    @NotNull(groups = {UpdateValidations.class})
    private String avatar;
    
    @PastOrPresent(groups = {UpdateValidations.class})
    private LocalDateTime registerDate;
    
    private String role;
    
    private boolean isBannedByAdmin;
    
}
