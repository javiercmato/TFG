package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import es.udc.fic.tfg.backendtfg.common.infrastructure.JacksonLocalDateTimeSerializer;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.CommentDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.UserSummaryDTO;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecipeDetailsDTO {
    @NotNull
    private UUID id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotNull
    @JsonSerialize(using = JacksonLocalDateTimeSerializer.class)
    private LocalDateTime creationDate;
    
    @NotNull
    private Long duration;
    
    private Integer diners;
    
    @JsonProperty(value = "isBannedByAdmin")
    private boolean isBannedByAdmin;
    
    private UUID categoryID;
    
    private UserSummaryDTO author;
    
    private List<RecipeIngredientDTO> ingredients;
    
    private List<RecipeStepDTO> steps;
    
    private List<RecipePictureDTO> pictures;
    
    private List<CommentDTO> comments;
    
    private Long totalVotes;
    
    private Float averageRating;
    
    private Integer version;
}
