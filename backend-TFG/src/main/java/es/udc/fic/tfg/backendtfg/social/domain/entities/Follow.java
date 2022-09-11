package es.udc.fic.tfg.backendtfg.social.domain.entities;

import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follow", schema = "social")
public class Follow {
    @EmbeddedId
    private FollowID id = new FollowID();
    
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @Column(name = "followDate", nullable = false)
    private LocalDateTime followDate;
    
    
    /* *************** Asociaciones con otras entidades *************** */
    @ManyToOne(optional = false)
    @MapsId("following")
    /** Usuario que comienza a seguir */
    private User following;
    
    @ManyToOne(optional = false)
    @MapsId("followed")
    /** Usuario que es seguido */
    private User followed;
    
}
