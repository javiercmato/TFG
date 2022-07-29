package es.udc.fic.tfg.backendtfg.ingredients.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.IngredientTypeDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IngredientTypeConversor {
    /* ******************** Convertir a DTO ******************** */
    public static IngredientTypeDTO toIngredientTypeDTO(IngredientType entity) {
        IngredientTypeDTO dto = new IngredientTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    
    
    /* ******************** Convertir a Entidad ******************** */
    
    
}
