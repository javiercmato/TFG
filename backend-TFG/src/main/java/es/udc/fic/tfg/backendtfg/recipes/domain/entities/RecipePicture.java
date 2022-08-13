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
@Table(schema = "recipes", name = "recipepicture")
public class RecipePicture {
    @EmbeddedId
    private RecipePictureID id;
    
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "picturedata", nullable = false)
    private byte[] pictureData;
    
    @ManyToOne
    @MapsId("recipeID")
    private Recipe recipe;
    
    
    /** Identificador de la entidad débil RecipePicture */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    static class RecipePictureID implements Serializable {
        @Column(table = "recipepicture", name = "recipe")
        private UUID recipeID;
        
        @Column(table = "recipepicture", name = "pictureorder")
        private Integer order;
    }
}
