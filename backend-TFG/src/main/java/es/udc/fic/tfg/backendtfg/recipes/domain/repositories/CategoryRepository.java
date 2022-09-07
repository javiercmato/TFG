package es.udc.fic.tfg.backendtfg.recipes.domain.repositories;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends PagingAndSortingRepository<Category, UUID> {
    
    boolean existsByNameIgnoreCase(String name);
    
    Optional<Category> findByNameIgnoreCase(String name);
    
}
