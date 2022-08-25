package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserSummaryDTO {
    private UUID userID;
    
    private String name;
    
    private String nickname;
    
}
