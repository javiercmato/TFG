package es.udc.fic.tfg.backendtfg.social.domain.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyFollowedException extends Exception {
    public final String nickname;                 // Nickname del usuario que ya está siendo seguido
    
    public UserAlreadyFollowedException(String nickname) {
        super(nickname);
        this.nickname = nickname;
    }
}
