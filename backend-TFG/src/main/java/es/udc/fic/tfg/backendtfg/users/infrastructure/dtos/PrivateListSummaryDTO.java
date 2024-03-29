package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PrivateListSummaryDTO {
    @NotNull
    private UUID id;
    
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
