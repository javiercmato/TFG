package es.udc.fic.tfg.backendtfg.social.domain.exceptions;

public class UserAlreadyFollowedException extends Exception {
    public UserAlreadyFollowedException(String nickname) {
        super(nickname);
    }
}
