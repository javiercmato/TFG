package es.udc.fic.tfg.backendtfg.users.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserSummaryDTO {
    private UUID userID;
    
    private String name;
    
    private String nickname;
    
}
