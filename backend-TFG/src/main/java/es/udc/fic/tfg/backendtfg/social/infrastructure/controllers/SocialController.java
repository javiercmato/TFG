package es.udc.fic.tfg.backendtfg.social.infrastructure.controllers;


import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.EntityNotFoundException;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.PermissionException;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
import es.udc.fic.tfg.backendtfg.social.application.SocialService;
import es.udc.fic.tfg.backendtfg.social.domain.entities.Comment;
import es.udc.fic.tfg.backendtfg.social.infrastructure.conversors.CommentConversor;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.CommentDTO;
import es.udc.fic.tfg.backendtfg.social.infrastructure.dtos.CreateCommentParamsDTO;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    
    
    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    
    
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
    
    
}
