package es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

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
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
}
