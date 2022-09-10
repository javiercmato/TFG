package es.udc.fic.tfg.backendtfg.social.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RateRecipeParamsDTO {
    @NotNull
    @PositiveOrZero
    private Integer rating;
    
    @NotNull
    private UUID userID;
}
