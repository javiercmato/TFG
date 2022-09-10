package es.udc.fic.tfg.backendtfg.social.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentParamsDTO {
    @NotBlank
    private String text;
    
    @NotNull
    private UUID userID;
}
