package es.udc.fic.tfg.backendtfg.social.infrastructure.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import es.udc.fic.tfg.backendtfg.common.infrastructure.JacksonLocalDateTimeSerializer;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    @NotNull
    private UUID id;
    
    @NotNull
    private boolean isRead;
    
    @NotNull
    @JsonSerialize(using = JacksonLocalDateTimeSerializer.class)
    @PastOrPresent
    private LocalDateTime createdAt;
    
    @NotNull
    private String title;
    
    @NotNull
    private String message;
}
