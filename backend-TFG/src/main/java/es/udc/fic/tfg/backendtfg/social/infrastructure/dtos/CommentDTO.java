package es.udc.fic.tfg.backendtfg.social.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import es.udc.fic.tfg.backendtfg.common.infrastructure.JacksonLocalDateTimeSerializer;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CommentDTO {
    @NotNull
    private UUID id;
    
    @NotNull
    private UserSummaryDTO author;
    
    @JsonSerialize(using = JacksonLocalDateTimeSerializer.class)
    @PastOrPresent
    private LocalDateTime creationDate;
    
    @NotBlank
    private String text;
    
    @JsonProperty(value = "isBannedByAdmin")
    private boolean banned;
}
