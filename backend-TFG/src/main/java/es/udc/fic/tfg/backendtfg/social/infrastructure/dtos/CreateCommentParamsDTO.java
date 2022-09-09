package es.udc.fic.tfg.backendtfg.social.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateCommentParamsDTO {
    @NotBlank
    private String text;
    
    @NotNull
    private UUID userID;
}
