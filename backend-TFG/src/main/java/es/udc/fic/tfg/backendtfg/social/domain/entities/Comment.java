package es.udc.fic.tfg.backendtfg.social.domain.entities;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment", schema = "social")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "postgresql-uuid-generator")
    @GenericGenerator(name="postgresql-uuid-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "creationdate", nullable = false)
    private LocalDateTime creationDate;
    
    @Column(name = "text", nullable = false)
    private String text;
    
    @Type(type = "org.hibernate.type.BooleanType")
    @Column(name = "isbannedbyadmin", nullable = false)
    private boolean isBannedByAdmin;
    
    
    @ManyToOne
    private User author;
    
    @ManyToOne
    private Recipe recipe;
    
    
    /* *************** Constructor *************** */
    public Comment(LocalDateTime creationDate, String text, boolean isBannedByAdmin, User author, Recipe recipe) {
        this.creationDate = creationDate;
        this.text = text;
        this.isBannedByAdmin = isBannedByAdmin;
        this.author = author;
        this.recipe = recipe;
    }
    
    /* *************** Domain-Model *************** */
    public void attachToRecipe(Recipe recipe) {
        recipe.addComment(this);
        this.setRecipe(recipe);
    }
}
