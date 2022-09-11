package es.udc.fic.tfg.backendtfg.social.infrastructure.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import es.udc.fic.tfg.backendtfg.common.infrastructure.JacksonLocalDateTimeSerializer;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FollowDTO {
    @NotNull
    private UserSummaryDTO following;
    
    @NotNull
    private UserSummaryDTO followed;
    
    @NotNull
    @JsonSerialize(using = JacksonLocalDateTimeSerializer.class)
    @PastOrPresent
    private LocalDateTime followDate;
}
