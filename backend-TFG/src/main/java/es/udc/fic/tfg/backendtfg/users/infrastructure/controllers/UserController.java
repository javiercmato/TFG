package es.udc.fic.tfg.backendtfg.users.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.users.application.services.UserService;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectPasswordException;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserControllerUtils controllerUtils;
    
    @Autowired
    private MessageSource messageSource;
    
    
    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    // Referencias a los errores en los ficheros de i18n
    public static final String INCORRECT_LOGIN_EXCEPTION_KEY            = "users.domain.exceptions.IncorrectLoginException";
    public static final String INCORRECT_PASSWORD_EXCEPTION_KEY         = "users.domain.exceptions.IncorrectPasswordException";
    

    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(IncorrectLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                INCORRECT_LOGIN_EXCEPTION_KEY, null, INCORRECT_LOGIN_EXCEPTION_KEY, locale
        );
        
        return new ErrorsDTO(errorMessage);
    }
    
    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleIncorrectPasswordException(IncorrectPasswordException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                INCORRECT_PASSWORD_EXCEPTION_KEY, null, INCORRECT_PASSWORD_EXCEPTION_KEY, locale
        );
        
        return new ErrorsDTO(errorMessage);
    }
    
    
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
            consumes = MediaType.APPLICATION_JSON_VALUE,
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
