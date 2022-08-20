package es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor.toUserSummaryDTO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipeConversor {
    /* ******************** Convertir a DTO ******************** */
    public static RecipeIngredientDTO toRecipeIngredientDTO(RecipeIngredient entity) {
        RecipeIngredientDTO dto = new RecipeIngredientDTO();
        dto.setId(entity.getIngredient().getId());
        dto.setName(entity.getIngredient().getName());
        dto.setMeasureUnit(entity.getMeasureUnit().toString());
        dto.setQuantity(entity.getQuantity());
        
        return dto;
    }
    
    public static RecipePictureDTO toRecipePictureDTO(RecipePicture entity) {
        String encodedPicture = Base64.getEncoder().encodeToString(entity.getPictureData());
        return new RecipePictureDTO(entity.getId().getOrder(), encodedPicture);
    }
    
    public static RecipeStepDTO toRecipeStepDTO(RecipeStep entity) {
        return new RecipeStepDTO(entity.getId().getStep(), entity.getText());
    }
    
    public static RecipeDTO toRecipeDTO(Recipe entity) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreationDate(entity.getCreationDate());
        dto.setDuration(entity.getDuration());
        dto.setDiners(entity.getDiners());
        dto.setBannedByAdmin(entity.isBannedByAdmin());
        dto.setAuthorID(entity.getAuthor().getId());
        dto.setCategoryID(entity.getCategory().getId());
        dto.setIngredients(toRecipeIngredientListDTO(entity.getIngredients()));
        dto.setPictures(toRecipePictureListDTO(entity.getPictures()));
        dto.setSteps(toRecipeStepListDTO(entity.getSteps()));
        
        return dto;
    }
    
    public static RecipeDetailsDTO toRecipeDetailsDTO(Recipe entity) {
        RecipeDetailsDTO dto = new RecipeDetailsDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreationDate(entity.getCreationDate());
        dto.setDuration(entity.getDuration());
        dto.setDiners(entity.getDiners());
        dto.setBannedByAdmin(entity.isBannedByAdmin());
        dto.setCategoryID(entity.getCategory().getId());
        dto.setAuthor(toUserSummaryDTO(entity.getAuthor()));
        dto.setIngredients(toRecipeIngredientListDTO(entity.getIngredients()));
        dto.setPictures(toRecipePictureListDTO(entity.getPictures()));
        dto.setSteps(toRecipeStepListDTO(entity.getSteps()));
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static List<RecipeIngredientDTO> toRecipeIngredientListDTO(List<RecipeIngredient> entityList) {
        return entityList.stream()
                         .map(RecipeConversor::toRecipeIngredientDTO)
                         .collect(Collectors.toList());
    }
    
    public static List<RecipePictureDTO> toRecipePictureListDTO(List<RecipePicture> entityList) {
        return entityList.stream()
                         .map(RecipeConversor::toRecipePictureDTO)
                         .collect(Collectors.toList());
    }
    
    public static List<RecipeStepDTO> toRecipeStepListDTO(List<RecipeStep> entityList) {
        return entityList.stream()
                         .map(RecipeConversor::toRecipeStepDTO)
                         .collect(Collectors.toList());
    }
    
    /* ******************** Convertir a Entidad ******************** */
    
    
    
}
