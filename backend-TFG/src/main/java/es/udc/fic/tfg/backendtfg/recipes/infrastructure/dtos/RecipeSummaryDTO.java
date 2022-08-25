package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class RecipeSummaryDTO {
    @NotNull
    private UUID id;
    
    @NotNull
    private UUID categoryID;
    
    private String picture;
    
    @NotNull
    private String name;
    
    private String description;
    
    @NotNull
    @JsonProperty(value = "isBannedByAdmin")
    private boolean isBannedByAdmin;
    
    @NotNull
    private Long duration;
    
    private Integer diners;
    
    @NotNull
    private Integer ingredientsCount;
}
