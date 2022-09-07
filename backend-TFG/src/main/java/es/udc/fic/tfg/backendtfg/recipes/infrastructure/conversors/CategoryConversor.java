package es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.CategoryDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryConversor {
    /* ******************** Convertir a DTO ******************** */
    public static CategoryDTO toCategoryDTO(Category entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        
        return dto;
    }
    
    /* ******************** Convertir a conjunto de DTO ******************** */
    public static List<CategoryDTO> toCategoryListDTO(List<Category> entityList) {
        return entityList.stream()
                .map( cat -> toCategoryDTO(cat))
                .collect(Collectors.toList());
    }
    
    /* ******************** Convertir a Entidad ******************** */
    
    
}
