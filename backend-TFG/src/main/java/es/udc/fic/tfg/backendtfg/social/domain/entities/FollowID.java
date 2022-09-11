package es.udc.fic.tfg.backendtfg.social.domain.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class FollowID implements Serializable {
    private static final long serialVersionUID = -2602079224703119759L;
    @Column(table = "follow", name = "following", nullable = false)
    private UUID following;
    
    @Column(table = "follow", name = "followed", nullable = false)
    private UUID followed;
}
