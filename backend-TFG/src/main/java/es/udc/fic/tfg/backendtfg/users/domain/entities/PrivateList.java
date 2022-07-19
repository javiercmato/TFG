package es.udc.fic.tfg.backendtfg.users.domain.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

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
    
    @ManyToOne(optional = false,
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorID")
    private User creator;
    
}
