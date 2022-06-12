package es.udc.fic.tfg.backendtfg.users.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.users.application.services.UserService;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.EntityAlreadyExistsException;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.AuthenticatedUserDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.SignUpParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserControllerUtils controllerUtils;
    
    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    
    
    /* ******************** ENDPOINTS ******************** */
    @PostMapping(path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthenticatedUserDTO> signUp(@Validated @RequestBody SignUpParamsDTO params)
            throws EntityAlreadyExistsException {
        // Parsear datos recibidos
        User parsedUser = UserConversor.fromSignUpParamsDTO(params);
        
        // Registrar usuario en servicio
        User signedUpUser = userService.signUp(parsedUser);
        
        // Generar contenido de la respuesta
        URI resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userID}")
                .buildAndExpand(signedUpUser.getId())
                .toUri();
        String serviceToken = controllerUtils.generateServiceTokenFromUser(signedUpUser);
        AuthenticatedUserDTO authenticatedUserDTO = UserConversor.toAuthenticatedUserDTO(signedUpUser, serviceToken);
        
        // Crea la respuesta HTTP y la envía
        return ResponseEntity
                .created(resourceLocation)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticatedUserDTO);
    }
    
    /* ******************** ENDPOINTS ******************** */
}
