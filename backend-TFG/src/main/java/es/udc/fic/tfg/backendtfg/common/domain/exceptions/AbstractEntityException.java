package es.udc.fic.tfg.backendtfg.common.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public abstract class AbstractEntityException extends Exception implements Serializable {
    @NotBlank
    private String entityName;
    
    @NotNull
    private Object key;
    
    protected AbstractEntityException(String message) {
        super(message);
    }
}
