package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "recipes", name = "privatelistrecipe")
public class PrivateListRecipe {
    @EmbeddedId
    private PrivateListRecipeID id = new PrivateListRecipeID();
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "insertiondate", nullable = false)
    private LocalDateTime insertionDate;
    
    @ManyToOne
    @MapsId("privateListID")
    private PrivateList privateList;
    
    @ManyToOne
    @MapsId("recipeID")
    private Recipe recipe;

}
