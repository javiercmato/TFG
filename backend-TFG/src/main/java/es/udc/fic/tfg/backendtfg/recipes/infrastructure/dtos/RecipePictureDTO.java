package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipePictureDTO {
    @NotNull
    @PositiveOrZero
    private Integer order;
    
    @NotNull
    private String pictureData;
}
