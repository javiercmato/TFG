package es.udc.fic.tfg.backendtfg.users.domain.entities;

import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.social.domain.entities.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usertable", schema = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "postgresql-uuid-generator")
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
            //cascade = CascadeType.PERSIST,
            orphanRemoval = true                // Borrar al usuario elimina también sus listas privadas
    )
    private Set<PrivateList> privateLists = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "author")
    private Set<Recipe> recipes = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "author")
    private Set<Comment> comments = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private Set<Rating> ratings = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "following", orphanRemoval = true)
    /** Usuarios a los que sigue el usuario actual */
    private Set<Follow> followings = new LinkedHashSet<>();
    
    @OneToMany(mappedBy = "followed", orphanRemoval = true)
    /** Seguidores del usuario actual */
    private Set<Follow> followers = new LinkedHashSet<>();
    
    @OneToMany(orphanRemoval = true)
    private Set<Notification> notifications = new LinkedHashSet<>();
    
    
    /* *************** DOMAIN-MODEL *************** */
    @Transient
    /** Devuelve las listas privadas del usuario */
    public List<PrivateList> getAllPrivateLists() {
        return privateLists.stream()
                .sorted((pl1, pl2) -> pl1.getTitle().compareToIgnoreCase(pl2.getTitle()))
                .collect(Collectors.toList());
    }
    
    /** Indica que la puntuación ha sido realizada por el usuario actual */
    public void addRating(Rating rate) {
        ratings.add(rate);
        rate.setAuthor(this);
    }
    
    /** Indica que el usuario ha obtenido un seguidor */
    public void addFollower(Follow follower) {
        followers.add(follower);
        follower.setFollowed(this);
    }
    
    /** Indica que el usuario ha perdido un seguidor */
    public void removeFollower(Follow follower) {
        followers.remove(follower);
        follower.setFollowed(null);
    }
    
    /** Indica que el usuario comienza a seguir a otro usuario */
    public void addFollowing(Follow following) {
        followings.add(following);
        following.setFollowing(this);
    }
    
    /** Indica que el usuario ha dejado de seguir a otro usuario */
    public void removeFollowing(Follow following) {
        followings.remove(following);
        following.setFollowing(null);
    }
}
