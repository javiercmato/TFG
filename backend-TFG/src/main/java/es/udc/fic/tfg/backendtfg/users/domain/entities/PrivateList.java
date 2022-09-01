package es.udc.fic.tfg.backendtfg.users.domain.entities;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.PrivateListRecipe;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "users", name = "privatelist")
public class PrivateList {
    @Id
    @GeneratedValue(generator = "postgresql-uuid-generator")
    @GenericGenerator(name="postgresql-uuid-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    
    @Column(name = "description", length = 100)
    private String description;
    
    
    
    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(optional = false,
            //cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorID")
    private User creator;
    
    @OneToMany(mappedBy = "privateList",
            orphanRemoval = true
    )
    private List<PrivateListRecipe> privateListRecipes = new ArrayList<>();
    
    /* *************** DOMAIN-MODEL *************** */
    /** Inserta la receta recibida en la lista */
    public void insertRecipe(PrivateListRecipe recipe) {
        privateListRecipes.add(recipe);
        recipe.setPrivateList(this);
    }
    
//    /** Elimina la receta de la lista */
//    public void removeRecipe(PrivateListRecipe recipe) {
//        privateListRecipes.remove(recipe);
//        recipe.setPrivateList(null);
//    }
}
