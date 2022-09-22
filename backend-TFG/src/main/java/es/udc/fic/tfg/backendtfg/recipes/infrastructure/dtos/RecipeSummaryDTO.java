package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
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
    
    private Float averageRating;
    
    @NotNull
    private UserSummaryDTO author;
}
