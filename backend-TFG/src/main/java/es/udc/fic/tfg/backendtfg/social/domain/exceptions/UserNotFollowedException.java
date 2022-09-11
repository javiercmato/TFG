package es.udc.fic.tfg.backendtfg.social.domain.exceptions;

public class UserNotFollowedException extends Exception {
    public final String nickname;                 // Nickname del usuario que no está siendo seguido
    
    public UserNotFollowedException(String nickname) {
        super(nickname);
        this.nickname = nickname;
    }
}
