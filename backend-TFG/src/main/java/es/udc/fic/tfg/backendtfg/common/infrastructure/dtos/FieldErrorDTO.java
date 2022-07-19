package es.udc.fic.tfg.backendtfg.common.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldErrorDTO {
    /** Nombre del campo que provoca el error */
    private String fieldName;
    
    /** Mensaje detallado con el problema */
    private String message;
}
