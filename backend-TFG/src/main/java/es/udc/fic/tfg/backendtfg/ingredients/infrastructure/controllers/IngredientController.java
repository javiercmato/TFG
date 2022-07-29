package es.udc.fic.tfg.backendtfg.ingredients.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.ingredients.application.IngredientService;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.Ingredient;
import es.udc.fic.tfg.backendtfg.ingredients.domain.entities.IngredientType;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.conversors.IngredientConversor;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.conversors.IngredientTypeConversor;
import es.udc.fic.tfg.backendtfg.ingredients.infrastructure.dtos.*;
import es.udc.fic.tfg.backendtfg.users.infrastructure.controllers.utils.UserControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private UserControllerUtils userControllerUtils;
    
    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    
    
    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    
    
    /* ******************** ENDPOINTS ******************** */
    @PostMapping(path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientDTO createIngredient(@Validated @RequestBody CreateIngredientParamsDTO params,
                                          @RequestAttribute("userID") UUID userID)
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Comprobar que el usuario actual y el que realiza la petición son el mismo
        if (!userControllerUtils.doUsersMatch(userID, params.getCreatorID()))
            throw new PermissionException();
        
        // Llamada al servicio
        Ingredient ingredient = ingredientService
                .createIngredient(params.getName(), params.getIngredientTypeID(), params.getCreatorID());
        
        // Generar respuesta
        return IngredientConversor.toIngredientDTO(ingredient);
    }
    
    @PostMapping(path = "/types",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientTypeDTO createIngredientTypeAsAdmin(@Validated @RequestBody CreateIngredientTypeParamsDTO params,
                                                         @RequestAttribute("userID") UUID adminID)
            throws PermissionException, EntityAlreadyExistsException, EntityNotFoundException {
        // Llamada al servicio
        IngredientType type = ingredientService.createIngredientTypeAsAdmin(params.getName(), adminID);
        
        // Generar respuesta
        return IngredientTypeConversor.toIngredientTypeDTO(type);
    }
    
}
