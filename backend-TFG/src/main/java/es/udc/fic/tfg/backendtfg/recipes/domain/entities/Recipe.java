package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recipe", schema = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "postgresql-uuid-generator")
    @GenericGenerator(name="postgresql-uuid-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @NotBlank
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "creationdate", nullable = false)
    private LocalDateTime creationDate;
    
    @Type(type = "org.hibernate.type.LongType")
    @Column(name = "duration", nullable = false)
    private Long duration;
    
    @Column(name = "diners")
    private Integer diners;
    
    @Type(type = "org.hibernate.type.BooleanType")
    @Column(name = "isbannedbyadmin", nullable = false)
    private boolean isBannedByAdmin;
    
    
    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "author")
    private User author;
    
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;
    
    
    @OneToMany(mappedBy = "recipe",
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<RecipeStep> steps = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "recipe",
            orphanRemoval = true
    )
    private List<RecipePicture> pictures = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "recipe",
            orphanRemoval = true
    )
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "recipe",
            orphanRemoval = true
    )
    private List<PrivateListRecipe> privateListRecipes = new ArrayList<>();
    
    
    
    /* *************** Domain-Model *************** */
    
    /**
     * Añade el paso recibido a la receta
     * @param step Paso a añadir
     */
    public void addStep(RecipeStep step) {
        steps.add(step);
        step.setRecipe(this);
    }
    
    /**
     * Añade la imagen recibida a la receta
     * @param picture Imagen a añadir
     */
    public void addPicture(RecipePicture picture) {
        pictures.add(picture);
        picture.setRecipe(this);
    }
    
    /**
     * Añade el ingrediente recibido a la receta
     * @param ingredient Ingrediente a añadir
     */
    public void addIngredient(RecipeIngredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }
}