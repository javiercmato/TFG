package es.udc.fic.tfg.backendtfg.ingredients.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.entities.Block;
import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.BlockDTO;
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

import java.util.List;
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
    
    @GetMapping(path = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<IngredientTypeDTO> getAllIngredientTypes() {
        // Llamada al servicio
        List<IngredientType> types = ingredientService.getIngredientTypes();
        
        // Generar respuesta
        return IngredientTypeConversor.toIngredientTypeListDTO(types);
    }
    
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
    
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public BlockDTO<IngredientSummaryDTO> findAllIngredients(@RequestParam(value = "page") int page,
                                                             @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        // Llamada al servicio
        Block<Ingredient> ingredientsBlock = ingredientService.findAllIngredients(page, pageSize);
    
        // Generar respuesta
        List<IngredientSummaryDTO> ingredientSummaryDTOList = IngredientConversor.toIngredientSummaryListDTO(ingredientsBlock.getItems());
    
        return createBlock(ingredientSummaryDTOList, ingredientsBlock.hasMoreItems(), ingredientsBlock.getItemsCount());
    }
    
    @GetMapping(value = "/find",
            params = "name",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BlockDTO<IngredientSummaryDTO> findIngredientsByName(@RequestParam("name") String name,
                                                                @RequestParam("page") int page,
                                                                @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        // Llamada al servicio
        Block<Ingredient> ingredientsBlock = ingredientService.findIngredientsByName(name, page, pageSize);
        
        // Generar respuesta
        List<IngredientSummaryDTO> ingredientSummaryDTOList = IngredientConversor.toIngredientSummaryListDTO(ingredientsBlock.getItems());
    
        return createBlock(ingredientSummaryDTOList, ingredientsBlock.hasMoreItems(), ingredientsBlock.getItemsCount());
    }
    
    @GetMapping(value = "/find",
            params = "typeID",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BlockDTO<IngredientSummaryDTO> findIngredientsByType(@RequestParam("typeID") UUID typeID,
                                                                @RequestParam("page") int page,
                                                                @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        // Llamada al servicio
        Block<Ingredient> ingredientsBlock = ingredientService.findIngredientsByType(typeID, page, pageSize);
        
        // Generar respuesta
        List<IngredientSummaryDTO> ingredientSummaryDTOList = IngredientConversor.toIngredientSummaryListDTO(ingredientsBlock.getItems());
        
        return createBlock(ingredientSummaryDTOList, ingredientsBlock.hasMoreItems(), ingredientsBlock.getItemsCount());
    }
    
    @GetMapping(value = "/find",
            params = {"name", "typeID"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BlockDTO<IngredientSummaryDTO> findIngredientsByNameAndType(@RequestParam(value = "typeID", required = false) UUID typeID,
                                                                       @RequestParam("name") String name,
                                                                       @RequestParam("page") int page,
                                                                       @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
        // Llamada al servicio
        Block<Ingredient> ingredientsBlock = ingredientService.findIngredientsByNameAndType(name, typeID, page, pageSize);
        
        // Generar respuesta
        List<IngredientSummaryDTO> ingredientSummaryDTOList = IngredientConversor.toIngredientSummaryListDTO(ingredientsBlock.getItems());
        
        return createBlock(ingredientSummaryDTOList, ingredientsBlock.hasMoreItems(), ingredientsBlock.getItemsCount());
    }
    
    
    
    /* ******************** FUNCIONES AUXILIARES ******************** */
    private <T> BlockDTO<T> createBlock(List<T> items, boolean hasMoreItems, int itemsCount) {
        BlockDTO<T> block = new BlockDTO<>();
        block.setItems(items);
        block.setItemsCount(itemsCount);
        block.setHasMoreItems(hasMoreItems);
        
        return block;
    }
}
