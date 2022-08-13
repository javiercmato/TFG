package es.udc.fic.tfg.backendtfg.ingredients.domain.entities;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.RecipeIngredient;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "ingredients", name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(generator = "postgresql-uuid-generator")
    @GenericGenerator(name="postgresql-uuid-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    
    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "ingredientType")
    private IngredientType ingredientType;
    
    
    @ManyToOne(optional = false,
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "creator", nullable = false)
    private User creator;
    
    @OneToMany(mappedBy = "recipe", orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    
}
