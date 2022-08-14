package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "recipes", name = "recipestep")
public class RecipeStep {
    @EmbeddedId
    private RecipeStepID id = new RecipeStepID();
    
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "text", nullable = false)
    private String text;
    
    @ManyToOne(optional = false)
    @MapsId("recipeID")             // Columna de la clave primaria que referencia a las recetas
    private Recipe recipe;
    
}
