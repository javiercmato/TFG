package es.udc.fic.tfg.backendtfg.users.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.RecipeConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.RecipeSummaryDTO;
import es.udc.fic.tfg.backendtfg.users.application.UserService;
import es.udc.fic.tfg.backendtfg.users.domain.entities.PrivateList;
import es.udc.fic.tfg.backendtfg.users.domain.entities.User;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectLoginException;
import es.udc.fic.tfg.backendtfg.users.domain.exceptions.IncorrectPasswordException;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.PrivateListConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.conversors.UserConversor;
import es.udc.fic.tfg.backendtfg.users.infrastructure.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

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
    public AuthenticatedUserDTO loginUsingToken(@RequestAttribute("userID") UUID userID, @RequestAttribute String token)
            throws EntityNotFoundException {
        // Inicia sesión en el servicio
        User user = userService.loginFromToken(userID);
        
        // Devuelve los datos del usuario junto al token recibido
        return UserConversor.toAuthenticatedUserDTO(user, token);
    }
    
    
    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthenticatedUserDTO login(@Validated @RequestBody LoginParamsDTO params) throws IncorrectLoginException {
        // Inicia sesión en el servicio
        User user = userService.login(params.getNickname(), params.getPassword());
        
        // Genera el token para el usuario
        String token = controllerUtils.generateServiceTokenFromUser(user);
        
        // Devuelve los datos del usuario junto al token recibido
        return UserConversor.toAuthenticatedUserDTO(user, token);
    }
    
    
    @PutMapping(path = "/{userID}/changePassword",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestAttribute("userID") UUID userID,
            @PathVariable("userID") UUID pathUserID,
            @Validated @RequestBody ChangePasswordParamsDTO params)
            throws PermissionException, IncorrectPasswordException, EntityNotFoundException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Actualizar contraseña en el servicio
        userService.changePassword(userID, params.getOldPassword(), params.getNewPassword());
    }
    
    
    @PutMapping(path = "/{userID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDTO updateProfile(@RequestAttribute("userID") UUID userID,
            @PathVariable("userID") UUID pathUserID,
            @Validated @RequestBody UpdateProfileParamsDTO params) throws PermissionException, EntityNotFoundException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Actualizar perfil en el el servicio
        User userData = UserConversor.fromUpdateProfileParamsDTO(params);
        User updatedUser = userService.updateProfile(userID, userData.getName(), userData.getSurname(), userData.getEmail(),
                                                     userData.getAvatar());
        
        // Generar respuesta
        return UserConversor.toUserDTO(updatedUser);
    }

    
    @DeleteMapping(path = "/{userID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteUser(@RequestAttribute("userID") UUID userID,
            @PathVariable("userID") UUID pathUserID) throws EntityNotFoundException, PermissionException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Eliminar usuario del servicio
        userService.deleteUser(userID);
        
        // Generar respuesta
        return ResponseEntity.noContent().build();
    }
    
    
    @GetMapping(path = "/{userID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDTO findUserByID(@PathVariable("userID") UUID userID) throws EntityNotFoundException {
        User user = userService.findUserById(userID);
        
        return UserConversor.toUserDTO(user);
    }
    
    
    @GetMapping(path = "/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDTO findUserByID(@RequestParam("nickname") String nickname) throws EntityNotFoundException {
        User user = userService.findUserByNickname(nickname);
        
        return UserConversor.toUserDTO(user);
    }
    
    
    @PutMapping(path = "/admin/ban/{userID}")
    public boolean banUserAsAdmin(@RequestAttribute("userID") UUID adminID,
            @PathVariable("userID") UUID targetUserID) throws EntityNotFoundException, PermissionException {
        // Banear al usuario
        return userService.banUserAsAdmin(adminID, targetUserID);
    }
    
    
    
    /* ****************************** LISTAS PRIVADAS ****************************** */
    @PostMapping(path = "/{userID}/lists",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PrivateListDTO createPrivateList(@Validated @RequestBody CreatePrivateListParamsDTO params,
                                            @RequestAttribute("userID") UUID userID,
                                            @PathVariable("userID") UUID pathUserID)
            throws EntityNotFoundException, PermissionException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Llamada al servicio
        PrivateList list = userService.createPrivateList(userID, params.getTitle(), params.getDescription());
        
        
        // Convertir datos y generar respuesta
        return PrivateListConversor.toPrivateListDTO(list);
    }
    
    @GetMapping(path = "/{userID}/lists",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PrivateListSummaryDTO> getPrivateListsByUser(@RequestAttribute("userID") UUID userID,
                                                            @PathVariable("userID") UUID pathUserID)
            throws PermissionException, EntityNotFoundException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Llamada al servicio
        List<PrivateList> userLists = userService.getPrivateListsByUser(userID);
        
        // Convertir datos y generar respuesta
        return PrivateListConversor.toPrivateListSummaryDTOList(userLists);
    }
    
    @GetMapping(path = "/{userID}/lists/{listID}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PrivateListDTO getPrivateListDetails(@RequestAttribute("userID") UUID userID,
                                                            @PathVariable("userID") UUID pathUserID,
                                                            @PathVariable("listID") UUID listID)
            throws PermissionException, EntityNotFoundException {
        // Comprobar que el usuario actual y el propietario de la lista son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Llamada al servicio
        PrivateList list = userService.findPrivateListByID(listID);
        List<Recipe> recipesInList = userService.getRecipesFromPrivateList(listID);
        
        // Convertir datos y generar respuesta
        PrivateListDTO dto = PrivateListConversor.toPrivateListDTO(list);
        List<RecipeSummaryDTO> recipeSummaryDTOList = RecipeConversor.toRecipeSummaryListDTO(recipesInList);
        dto.setRecipes(recipeSummaryDTOList);
        
        return dto;
    }
    
    @PostMapping(path = "/{userID}/lists/{listID}/add/{recipeID}")
    @ResponseStatus(HttpStatus.OK)
    public void addRecipeToPrivateList(@RequestAttribute("userID") UUID userID,
                                       @PathVariable("userID") UUID pathUserID,
                                       @PathVariable("listID") UUID listID,
                                       @PathVariable("recipeID") UUID recipeID)
            throws EntityNotFoundException, PermissionException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Llamada al servicio
        userService.addRecipeToPrivateList(listID, recipeID);
    }
    
    @DeleteMapping(path = "/{userID}/lists/{listID}/remove/{recipeID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRecipeFromPrivateList(@RequestAttribute("userID") UUID userID,
            @PathVariable("userID") UUID pathUserID,
            @PathVariable("listID") UUID listID,
            @PathVariable("recipeID") UUID recipeID)
            throws EntityNotFoundException, PermissionException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Llamada al servicio
        userService.removeRecipeFromPrivateList(listID, recipeID);
    }
    
    @DeleteMapping(path = "/{userID}/lists/{listID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrivateList(@RequestAttribute("userID") UUID userID,
            @PathVariable("userID") UUID pathUserID,
            @PathVariable("listID") UUID listID)
            throws EntityNotFoundException, PermissionException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, pathUserID))
            throw new PermissionException();
        
        // Llamada al servicio
        userService.deletePrivateList(listID);
    }
    
}
