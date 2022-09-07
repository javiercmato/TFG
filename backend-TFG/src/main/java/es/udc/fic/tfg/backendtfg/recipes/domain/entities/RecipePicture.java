package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "recipes", name = "recipepicture")
public class RecipePicture {
    @EmbeddedId
    private RecipePictureID id = new RecipePictureID();
    
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "picturedata", nullable = false)
    private byte[] pictureData;
    
    @ManyToOne
    @MapsId("recipeID")
    private Recipe recipe;
    
}
