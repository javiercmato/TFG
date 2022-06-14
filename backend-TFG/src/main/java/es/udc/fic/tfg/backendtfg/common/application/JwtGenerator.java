package es.udc.fic.tfg.backendtfg.common.application;

import es.udc.fic.tfg.backendtfg.common.domain.jwt.JwtData;

public interface JwtGenerator {
    /**
     * Genera un String que representa a un Json Web Token.
     * @param data JWT
     * @return JWT convertido a String
     */
    String generateJWT(JwtData data);
    
    /**
     * Extrae la información de un Json Web Token.
     * @param token String que representa al JWT
     * @return JWT parseado
     */
    JwtData extractInfo(String token);
}
