package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserSummaryDTO {
    private UUID userID;
    
    private String name;
    
    private String nickname;
    
}
