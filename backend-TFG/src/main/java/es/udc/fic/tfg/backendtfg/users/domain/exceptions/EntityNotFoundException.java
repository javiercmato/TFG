package es.udc.fic.tfg.backendtfg.users.domain.exceptions;

public class EntityNotFoundException extends AbstractEntityException {
    public EntityNotFoundException(String entityName, Object key) {
        super(entityName, key);
    }
}
