package es.udc.fic.tfg.backendtfg.ingredients.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.IngredientDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
    
    /* ******************** Convertir a Entidad ******************** */
    
    
}
