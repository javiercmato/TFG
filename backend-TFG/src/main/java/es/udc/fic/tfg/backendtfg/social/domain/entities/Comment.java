package es.udc.fic.tfg.backendtfg.social.domain.entities;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment", schema = "social")
public class Comment {
    @EmbeddedId
    private CommentID id = new CommentID();
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "creationdate", nullable = false)
    private LocalDateTime creationDate;
    
    @Column(name = "text", nullable = false)
    private String text;
    
    @Type(type = "org.hibernate.type.BooleanType")
    @Column(name = "isbannedbyadmin", nullable = false)
    private boolean isBannedByAdmin;
    
    
    @ManyToOne
    @MapsId("authorID")
    private User author;
    
    @ManyToOne
    @MapsId("recipeID")
    private Recipe recipe;
}
