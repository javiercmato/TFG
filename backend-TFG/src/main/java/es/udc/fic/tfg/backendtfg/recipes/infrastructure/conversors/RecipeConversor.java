package es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.*;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.social.infrastructure.conversors.CommentConversor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
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
        dto.setTotalVotes(entity.getTotalVotes());
        dto.setAverageRating(entity.getAverageRating());
        dto.setVersion(entity.getVersion());
        dto.setComments(CommentConversor.toCommentDTOList(entity.getComments()));
        
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
        dto.setTotalVotes(entity.getTotalVotes());
        dto.setAverageRating(entity.getAverageRating());
        dto.setVersion(entity.getVersion());
        
        return dto;
    }
    
    public static RecipeSummaryDTO toRecipeSummaryDTO(Recipe entity) {
        RecipeSummaryDTO dto = new RecipeSummaryDTO();
        dto.setId(entity.getId());
        dto.setCategoryID(entity.getCategory().getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setDuration(entity.getDuration());
        dto.setDiners(entity.getDiners());
        dto.setBannedByAdmin(entity.isBannedByAdmin());
        // Codificar imágen (si hay)
        if (!entity.getPictures().isEmpty()) {
            RecipePictureDTO pictureDTO = toRecipePictureDTO(
                entity.getPictures().stream()
                    .findFirst()
                    .get());
            dto.setPicture(pictureDTO.getPictureData());
        } else {
            dto.setPicture(null);
        }
        dto.setIngredientsCount(entity.getIngredients().size());
        dto.setAverageRating(entity.getAverageRating());
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static List<RecipeIngredientDTO> toRecipeIngredientListDTO(Set<RecipeIngredient> entitySet) {
        return entitySet.stream()
                        .map(RecipeConversor::toRecipeIngredientDTO)
                        .collect(Collectors.toList());
    }
    
    public static List<RecipePictureDTO> toRecipePictureListDTO(Set<RecipePicture> entitSet) {
        return entitSet.stream()
                        .map(RecipeConversor::toRecipePictureDTO)
                        .collect(Collectors.toList());
    }
    
    public static List<RecipeStepDTO> toRecipeStepListDTO(Set<RecipeStep> entitySet) {
        return entitySet.stream()
                        .map(RecipeConversor::toRecipeStepDTO)
                        .collect(Collectors.toList());
    }
    
    public static List<RecipeSummaryDTO> toRecipeSummaryListDTO(List<Recipe> entityList) {
        return entityList.stream()
                         .map(RecipeConversor::toRecipeSummaryDTO)
                         .collect(Collectors.toList());
    }
    /* ******************** Convertir a Entidad ******************** */
    
}
