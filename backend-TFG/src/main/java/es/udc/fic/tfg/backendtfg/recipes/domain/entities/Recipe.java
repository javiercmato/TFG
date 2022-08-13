package es.udc.fic.tfg.backendtfg.recipes.domain.entities;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

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
    
}
