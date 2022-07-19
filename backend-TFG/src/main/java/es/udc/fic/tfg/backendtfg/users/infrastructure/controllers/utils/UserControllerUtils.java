package es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils;

import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import es.udc.fic.tfg.backendtfg.common.domain.jwt.JwtData;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserControllerUtils {
    @Autowired
    private JwtGenerator jwtGenerator;
    
    /**
     * Genera un JWT para el usuario recibido */
    public String generateServiceTokenFromUser(User user) {
        JwtData jwtData = new JwtData(user.getId(), user.getNickname(), user.getRole().toString());
        
        return jwtGenerator.generateJWT(jwtData);
    }
    
    /** Comprueba si dos ID de usuario coinciden */
    public boolean doUsersMatch(UUID requestUserID, UUID targetUserID) {
        return requestUserID.equals(targetUserID);
    }
}
