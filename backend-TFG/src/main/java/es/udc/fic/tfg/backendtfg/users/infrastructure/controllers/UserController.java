package es.udc.fic.tfg.backendtfg.users.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.users.application.services.UserService;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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
    
    
    @PostMapping(path = "/login/token",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthenticatedUserDTO loginUsingToken(@RequestAttribute UUID userID, @RequestAttribute String token)
            throws ResourceBannedByAdministratorException, EntityNotFoundException {
        // Inicia sesión en el servicio
        User user = userService.loginFromToken(userID);
        
        // Devuelve los datos del usuario junto al token recibido
        return UserConversor.toAuthenticatedUserDTO(user, token);
    }
    
    
    @PostMapping(path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthenticatedUserDTO login(@Validated @RequestBody LoginParamsDTO params)
            throws IncorrectLoginException, ResourceBannedByAdministratorException {
        // Inicia sesión en el servicio
        User user = userService.login(params.getNickname(), params.getPassword());
        
        // Genera el token para el usuario
        String token = controllerUtils.generateServiceTokenFromUser(user);
        
        // Devuelve los datos del usuario junto al token recibido
        return UserConversor.toAuthenticatedUserDTO(user, token);
    }
    

}
