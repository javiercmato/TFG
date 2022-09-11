package es.udc.fic.tfg.backendtfg.social.infrastructure.controllers;


import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.PermissionException;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import es.udc.fic.tfg.backendtfg.recipes.domain.entities.Recipe;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.conversors.RecipeConversor;
import es.udc.fic.tfg.backendtfg.recipes.infrastructure.dtos.RecipeDetailsDTO;
import es.udc.fic.tfg.backendtfg.social.application.SocialService;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Follow;
import es.udc.fic.tfg.backendtfg.social.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.social.infrastructure.conversors.CommentConversor;
import es.udc.fic.tfg.backendtfg.social.infrastructure.conversors.FollowConversor;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/social")
public class SocialController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private UserControllerUtils controllerUtils;
    @Autowired
    private SocialService socialService;
    @Autowired
    private MessageSource messageSource;
    
    
    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    // Referencias a los errores en los ficheros de i18n
    public static final String RECIPE_ALREADY_RATED_EXCEPTION_KEY           = "social.domain.exceptions.RecipeAlreadyRatedException";
    public static final String USER_ALREADY_FOLLOWED_EXCEPTION_KEY          = "social.domain.exceptions.UserAlreadyFollowedException";
    
    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(RecipeAlreadyRatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleRecipeAlreadyRatedException(RecipeAlreadyRatedException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(
                RECIPE_ALREADY_RATED_EXCEPTION_KEY, null, RECIPE_ALREADY_RATED_EXCEPTION_KEY, locale
        );
    
        return new ErrorsDTO(errorMessage);
    }
    
    @ExceptionHandler(UserAlreadyFollowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleUserAlreadyFollowedException(UserAlreadyFollowedException exception, Locale locale) {
        String exceptionMessage = messageSource.getMessage(
                exception.getNickname(), null, exception.getNickname(), locale
        );
        String globalErrorMessage = messageSource.getMessage(
                USER_ALREADY_FOLLOWED_EXCEPTION_KEY,
                new Object[] {exceptionMessage},
                USER_ALREADY_FOLLOWED_EXCEPTION_KEY,
                locale
        );
    
        return new ErrorsDTO(globalErrorMessage);
    }
    
    
    
    /* ******************** ENDPOINTS ******************** */
    @PostMapping(path = "/comments/{recipeID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO commentRecipe(@Validated @RequestBody CreateCommentParamsDTO params,
            @PathVariable("recipeID") UUID recipeID,
            @RequestAttribute("userID") UUID userID)
            throws PermissionException, EntityNotFoundException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, params.getUserID()))
            throw new PermissionException();
        
        // Llamada al servicio
        Comment comment = socialService.commentRecipe(userID, recipeID, params.getText());
        
        // Generar respuesta
        return CommentConversor.toCommentDTO(comment);
    }
    
    @GetMapping(path = "/comments/{recipeID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BlockDTO<CommentDTO> getRecipeComments(@PathVariable("recipeID") UUID recipeID,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("pageSize") int pageSize)
        throws EntityNotFoundException {
        // Llamada al servicio
        Block<Comment> block = socialService.getRecipeComments(recipeID, page, pageSize);
        
        // Generar respuesta
        return CommentConversor.toCommentBlockDTO(block);
    }
    
    @PutMapping(path = "/comments/admin/ban/{commentID}")
    public CommentDTO banCommentAsAdmin(@RequestAttribute("userID") UUID adminID,
                                     @PathVariable("commentID") UUID targetCommentID)
            throws PermissionException, EntityNotFoundException {
        // Llamada al servicio
        Comment comment = socialService.banCommentAsAdmin(adminID, targetCommentID);
        
        // Generar respuesta
        return CommentConversor.toCommentDTO(comment);
    }
    
    @PostMapping(path = "/rate/{recipeID}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RecipeDetailsDTO rateRecipe(@Validated @RequestBody RateRecipeParamsDTO params,
                                       @RequestAttribute("userID") UUID userID,
                                       @PathVariable("recipeID") UUID recipeID)
            throws PermissionException, EntityNotFoundException, RecipeAlreadyRatedException {
        // Comprobar que el usuario actual y el usuario objetivo son el mismo
        if (!controllerUtils.doUsersMatch(userID, params.getUserID()))
            throw new PermissionException();
    
        // Llamada al servicio
        Recipe ratedRecipe = socialService.rateRecipe(userID, recipeID, params.getRating());
        
        // Generar respuesta
        return RecipeConversor.toRecipeDetailsDTO(ratedRecipe);
    }
    
    @PutMapping(path = "/follow/{requestorID}/{targetID}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public FollowDTO followUser(@RequestAttribute("userID") UUID userID,
                                @PathVariable("requestorID") UUID requestorID,
                                @PathVariable("targetID") UUID targetID)
            throws PermissionException, UserAlreadyFollowedException, EntityNotFoundException {
        // Comprobar que el usuario actual y el usuario que realiza la operación son el mismo
        if (!controllerUtils.doUsersMatch(userID, requestorID))
            throw new PermissionException();
        
        // Llamada al servicio
        Follow follow = socialService.followUser(userID, targetID);
        
        // Generar respuesta
        return FollowConversor.toFollowDTO(follow);
    }
    
    @DeleteMapping(path = "/unfollow/{requestorID}/{targetID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unfollowUser(@RequestAttribute("userID") UUID userID,
            @PathVariable("requestorID") UUID requestorID,
            @PathVariable("targetID") UUID targetID)
            throws PermissionException, EntityNotFoundException, UserNotFollowedException {
        // Comprobar que el usuario actual y el usuario que realiza la operación son el mismo
        if (!controllerUtils.doUsersMatch(userID, requestorID))
            throw new PermissionException();
        
        // Llamada al servicio
        socialService.unfollowUser(userID, targetID);
    
        // Generar respuesta
        return ResponseEntity.noContent().build();
    }
}
