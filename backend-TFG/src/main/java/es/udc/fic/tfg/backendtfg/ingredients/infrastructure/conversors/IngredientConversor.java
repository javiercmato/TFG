package es.udc.fic.tfg.backendtfg.ingredients.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.IngredientDTO;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.IngredientSummaryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IngredientConversor {
    /* ******************** Convertir a DTO ******************** */
    public static IngredientDTO toIngredientDTO(Ingredient entity) {
        IngredientDTO dto = new IngredientDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIngredientTypeID(entity.getIngredientType().getId());
        dto.setCreatorID(entity.getCreator().getId());
        
        return dto;
    }
    
    public static IngredientSummaryDTO toIngredientSummaryDTO(Ingredient entity) {
        IngredientSummaryDTO dto = new IngredientSummaryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static List<IngredientSummaryDTO> toIngredientSummaryListDTO(List<Ingredient> entityList) {
        return entityList.stream()
                .map(ingredient -> toIngredientSummaryDTO(ingredient))
                .collect(Collectors.toList());
    }
    
    /* ******************** Convertir a Entidad ******************** */
    
    
}
