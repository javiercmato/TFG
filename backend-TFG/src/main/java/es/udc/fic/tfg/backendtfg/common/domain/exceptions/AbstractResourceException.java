package es.udc.fic.tfg.backendtfg.common.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public abstract class AbstractResourceException extends Exception implements Serializable {
}
