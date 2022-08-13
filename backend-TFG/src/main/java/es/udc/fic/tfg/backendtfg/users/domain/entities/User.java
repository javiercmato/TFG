package es.udc.fic.tfg.backendtfg.users.domain.entities;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "users", name = "usertable")
public class User {
    @Id
    @GeneratedValue(generator = "postgresql-uuid-generator")
    @GenericGenerator(name="postgresql-uuid-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @NotBlank
    @Column(name = "nickname", nullable = false, unique = true, length = 30)
    private String nickname;
    
    @NotBlank
    @Column(name = "password", nullable = false, length = 30)
    private String password;
    
    @NotBlank
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @NotNull
    @Column(name = "surname", length = 50)
    private String surname;
    
    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "avatar")
    private byte[] avatar;
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "registerdate", nullable = false)
    private LocalDateTime registerDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
    
    @Type(type = "org.hibernate.type.BooleanType")
    @Column(name = "isbannedbyadmin", nullable = false)
    private boolean isBannedByAdmin;
    
    
    /* *************** Asociaciones con otras entidades *************** */
    @OneToMany(mappedBy = "creator",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true                // Borrar al usuario elimina también sus listas privadas
    )
    private Set<PrivateList> privateLists = new HashSet<>();
    
    @OneToMany(mappedBy = "author")
    private Set<Recipe> recipes = new LinkedHashSet<>();
    
}
