package es.udc.fic.tfg.backendtfg.users.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    
    @NotNull
    @Column(name = "nickname", nullable = false, unique = true, length = 30)
    private String nickname;
    
    @NotNull
    @Column(name = "password", nullable = false, length = 30)
    private String password;
    
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @NotNull
    @Column(name = "surname", length = 50)
    private String surname;
    
    @NotNull
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "avatar")
    private byte[] avatar;
    
    @Column(name = "registerdate", nullable = false)
    private Date registerDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
    
    /* *************** Asociaciones con otras entidades *************** */
    @OneToMany(mappedBy = "creator",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private Set<PrivateList> privateLists = new HashSet<>();
}
