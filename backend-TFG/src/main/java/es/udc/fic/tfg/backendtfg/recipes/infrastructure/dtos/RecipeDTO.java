package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import es.udc.fic.tfg.backendtfg.common.infrastructure.JacksonLocalDateTimeSerializer;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.CommentDTO;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RecipeDTO {
    @NotNull
    private UUID id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotNull
    @JsonSerialize(using = JacksonLocalDateTimeSerializer.class)
    @PastOrPresent
    private LocalDateTime creationDate;
    
    @NotNull
    private Long duration;
    
    private Integer diners;
    
    @JsonProperty(value = "isBannedByAdmin")
    private boolean isBannedByAdmin;
    
    private UUID authorID;
    
    private UUID categoryID;
    
    private List<RecipeIngredientDTO> ingredients;
    
    private List<RecipePictureDTO> pictures;
    
    private List<RecipeStepDTO> steps;
    
    private List<CommentDTO> comments;
    
    private Long totalVotes;
    
    private Float averageRating;
}
