package es.udc.fic.tfg.backendtfg.users.domain.exceptions;

public class EntityAlreadyExistsException extends AbstractEntityException {
    public EntityAlreadyExistsException(String entityName, Object key) {
        super(entityName, key);
    }
}
