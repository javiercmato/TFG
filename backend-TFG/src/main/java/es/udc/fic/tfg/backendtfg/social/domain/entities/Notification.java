package es.udc.fic.tfg.backendtfg.social.domain.entities;

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
@Entity
@Table(name = "notification", schema = "social")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "postgresql-uuid-generator")
    @GenericGenerator(name="postgresql-uuid-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @Type(type = "org.hibernate.type.BooleanType")
    @Column(name = "isread", nullable = false)
    private boolean isRead;
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "message", nullable = false)
    private String message;
    
    
    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(optional = false)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;
    
}
