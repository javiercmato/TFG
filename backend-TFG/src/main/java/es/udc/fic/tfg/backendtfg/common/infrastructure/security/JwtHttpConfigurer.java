package es.udc.fic.tfg.backendtfg.common.infrastructure.security;

import es.udc.fic.tfg.backendtfg.common.application.JwtGenerator;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

// https://stackoverflow.com/a/71449312/11295728
@Component
@AllArgsConstructor
public class JwtHttpConfigurer extends AbstractHttpConfigurer<JwtHttpConfigurer, HttpSecurity> {
    private final JwtGenerator jwtGenerator;
    
    @Override
    public void configure(HttpSecurity http) throws Exception{
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        JwtFilter jwtFilter = new JwtFilter(authenticationManager, jwtGenerator);
        http.addFilter(jwtFilter);
    }
    
}
