package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "recipes", name = "recipestep")
public class RecipeStep {
    @EmbeddedId
    private RecipeStepID id;
    
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "text", nullable = false)
    private String text;
    
    @ManyToOne(optional = false)
    @MapsId("recipeID")                 // Indica qué columna de la clave primaria hace referencia a las recetas
    private Recipe recipe;
    
    
    /** Identificador de la entidad débil RecipeStep */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    static class RecipeStepID implements Serializable {
        @Column(table = "recipestep", name = "recipe")
        private UUID recipeID;
        
        @Column(table = "recipestep", name = "step")
        private Integer step;
    }
}
