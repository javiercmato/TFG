package es.udc.fic.tfg.backendtfg.common.domain.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtData {
    private UUID userID;
    private String nickname;
    private String role;
}
