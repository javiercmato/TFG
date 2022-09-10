package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Rating;
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
@EqualsAndHashCode
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
    
    @Type(type = "org.hibernate.type.LongType")
    @Column(name = "totalvotes", nullable = false)
    private Long totalVotes = Long.valueOf(0);
    
    @Type(type = "org.hibernate.type.FloatType")
    @Column(name= "averagerating", nullable = false)
    private Float averageRating = Float.valueOf(0);
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    
    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author")
    private User author;
    
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;
    
    
    @OneToMany(mappedBy = "recipe",
            //fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<RecipeStep> steps = new HashSet<>();
    
    
    @OneToMany(mappedBy = "recipe",
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<RecipePicture> pictures = new HashSet<>();
    
    
    @OneToMany(mappedBy = "recipe",
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<RecipeIngredient> ingredients = new HashSet<>();
    
    
    @OneToMany(mappedBy = "recipe",
            orphanRemoval = true
    )
    private Set<PrivateListRecipe> privateListRecipes = new HashSet<>();
    
    @OneToMany(mappedBy = "recipe",
            orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
    
    @OneToMany(orphanRemoval = true)
    private Set<Rating> ratings = new LinkedHashSet<>();
    
    
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
    
    /** Indica que la receta ha sido añadida a una lista privada */
    public void insertToPrivateList(PrivateListRecipe plr) {
        privateListRecipes.add(plr);
        plr.setRecipe(this);
    }
    
    /** Indica que la receta ha sido retirada de la lista privada */
    public void removeFromPrivateList(PrivateListRecipe plr) {
        plr.setRecipe(null);
        privateListRecipes.remove(plr);
    }
    
    /**
     * Añade el comentario recibido a la receta.
     * @param comment Comentario a añadir
     */
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setRecipe(this);
    }
    
    /** Añade una nueva votación y calcula el valor medio */
    public float rate(Rating rate) {
        ratings.add(rate);
        rate.setRecipe(this);
        
        //this.totalVotes += 1;
        long votesCount = ratings.size();
        float average = ((averageRating*(votesCount - 1) + rate.getValue())) / votesCount;
        this.averageRating = average;
        
        return average;
    }
    
}
