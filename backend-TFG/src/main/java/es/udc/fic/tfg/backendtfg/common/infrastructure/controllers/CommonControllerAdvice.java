package es.udc.fic.tfg.backendtfg.common.infrastructure.controllers;

import es.udc.fic.tfg.backendtfg.common.domain.exceptions.*;
import es.udc.fic.tfg.backendtfg.common.infrastructure.dtos.ErrorsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@ControllerAdvice
public class CommonControllerAdvice {
    @Autowired
    private MessageSource messageSource;
    
    
    /* ******************** TRADUCCIONES DE EXCEPCIONES ******************** */
    // Referencias a los errores en los ficheros de i18n
    public static final String ENTITY_ALREADY_EXISTS_EXCEPTION_KEY              = "common.domain.exceptions.EntityAlreadyExistsException";
    public static final String ENTITY_NOT_FOUND_EXCEPTION_KEY                   = "common.domain.exceptions.EntityNotFoundException";
    public static final String PERMISION_EXCEPTION_KEY                          = "common.domain.exceptions.PermissionException";
    public static final String RESOURCE_BANNED_BY_ADMINISTRATOR_EXCEPTION_KEY   = "common.domain.exceptions.ResourceBannedByAdministratorException";
    
    
    
    /* ******************** MANEJADORES DE EXCEPCIONES ******************** */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    @ResponseBody
    public ErrorsDTO handleEntityAlreadyExistsException(EntityAlreadyExistsException exception, Locale locale) {
        String exceptionMessage = messageSource.getMessage(
                exception.getEntityName(), null, exception.getEntityName(), locale
        );
        String globalErrorMessage = messageSource.getMessage(
                ENTITY_ALREADY_EXISTS_EXCEPTION_KEY,
                new Object[] {exceptionMessage, exception.getKey().toString()},
                ENTITY_ALREADY_EXISTS_EXCEPTION_KEY,
                locale
        );
        
        return new ErrorsDTO(globalErrorMessage);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)     // 404
    @ResponseBody
    public ErrorsDTO handleEntityNotFoundException(EntityNotFoundException exception, Locale locale) {
        String exceptionMessage = messageSource.getMessage(
                exception.getEntityName(), null, exception.getEntityName(), locale
        );
        String globalErrorMessage = messageSource.getMessage(
                ENTITY_NOT_FOUND_EXCEPTION_KEY,
                new Object[] {exceptionMessage, exception.getKey().toString()},
                ENTITY_NOT_FOUND_EXCEPTION_KEY,
                locale
        );
        
        return new ErrorsDTO(globalErrorMessage);
    }
    
    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)     // 403
    @ResponseBody
    public ErrorsDTO handlePermissionException(PermissionException exception, Locale locale) {
        String globalErrorMessage = messageSource.getMessage(
                PERMISION_EXCEPTION_KEY, null, PERMISION_EXCEPTION_KEY, locale
        );
        
        return new ErrorsDTO(globalErrorMessage);
    }
    
    @ExceptionHandler(ResourceBannedByAdministratorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)     // 404
    @ResponseBody
    public ErrorsDTO handleResourceBannedByAdministratorException(ResourceBannedByAdministratorException exception, Locale locale) {
        String globalErrorMessage = messageSource.getMessage(
                RESOURCE_BANNED_BY_ADMINISTRATOR_EXCEPTION_KEY, null, RESOURCE_BANNED_BY_ADMINISTRATOR_EXCEPTION_KEY, locale
        );
        
        return new ErrorsDTO(globalErrorMessage);
    }
    
}
