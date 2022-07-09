package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
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
    
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @PastOrPresent(groups = {UpdateValidations.class})
    private LocalDateTime registerDate;
    
    private String role;
    
    @JsonProperty(value = "isBannedByAdmin")
    private boolean isBannedByAdmin;
    
}
