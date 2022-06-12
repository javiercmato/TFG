package es.udc.fic.tfg.backendtfg.users.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public abstract class AbstractResourceException extends Exception implements Serializable {
    protected AbstractResourceException(String message) {
        super(message);
    }
}
