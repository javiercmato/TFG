package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "recipes", name = "privatelistrecipe")
public class PrivateListRecipe {
    @EmbeddedId
    private PrivateListRecipeID id;
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "insertiondate", nullable = false)
    private LocalDateTime insertionDate;
    
    @ManyToOne
    @MapsId("privateListID")
    private PrivateList privateList;
    
    @ManyToOne
    @MapsId("recipeID")
    private Recipe recipe;
    
    
    /** Identificador de la relación N:M entre PrivateList y Recipe */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    static class PrivateListRecipeID implements Serializable {
        @Column(table = "privatelistrecipe", name = "privateList")
        private UUID privateListID;
    
        @Column(table = "privatelistrecipe", name = "recipe")
        private UUID recipeID;
    }
}
