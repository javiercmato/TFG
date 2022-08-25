package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipePictureParamsDTO {
    @NotNull
    @Positive
    private int order;
    
    @NotNull
    private String data;
}
