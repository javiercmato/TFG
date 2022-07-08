import {NetworkErrorException} from "./exceptions";
import {handle2xxResponse, handle4xxResponse} from './responseHandlers';

/** URL del backend extraído de la configuración del entorno */
const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
const SERVICE_TOKEN_NAME = process.env.REACT_APP_SERVICE_TOKEN_NAME!;

let onReauthenticationCallback : CallbackFunction;
let onNetworkErrorCallback : CallbackFunction;

/* ****************************************** SERVICIOS ***************************************** */
/**
 * Configura la petición que se realizará al backend.
 * @param {String} method - Tipo de petición HTTP: {GET, POST, PUT, etc}
 * @param {Object} body - Contenido a enviar en el cuerpo de la petición
 * @returns {Object} - Configuración para realizar la petición
 */
export const configFetchParameters = (method: string, body?: Record<string, any>): RequestInit => {
    const configuration : RequestInit = {};
    configuration.headers = new Headers();

    // Establecer tipo de petición HTTP
    if (method)
        configuration.method = method;

    if (body) {
        // Si contenido es un formulario, lo envía sin modificarlo
        if (body instanceof FormData)
            configuration.body = body;
        // Sino, convierte el contenido a JSON
        else {
            configuration.headers.append('Content-Type', 'application/json');
            configuration.body = JSON.stringify(body);
        }
    }

    // Insertar el JWT en las cabeceras de la petición si está disponible
    const serviceToken = getServiceToken();
    if (serviceToken) {
        // Si hay cabeceras, añade el token. Sino, crea las cabeceras y añade el token
        if (configuration.headers) {
            configuration.headers.append('Authorization', `Bearer ${serviceToken}`);
        } else {
            configuration.headers = new Headers();
            configuration.headers.append('Authorization', `Bearer ${serviceToken}`);
        }
    }

    // Indicar en cabeceras el idioma que usa el navegador
    let hasDefaultLocale = navigator.language;                                  // Comprobar si navegador tiene un idioma principal por defecto
    let hasDefaultLocales = navigator.languages && navigator.languages[0];      // Comprobar si navegador tiene idiomas configurados
    const defaultLocale = 'es';                                                 // Idioma por defecto
    let locale = hasDefaultLocales || hasDefaultLocale || defaultLocale;
    if (configuration.headers) {
        configuration.headers.append('Accept-Language', locale);
    }

    return configuration;
}

/**
 * Realiza las peticiones al backend usando la configuración recibida
 * @param {String} endpoint - Endpoint al que realizar la petición
 * @param {RequestInit} configuration - Configuración de la petición (emplear configFetchParameters())
 * @param {CallbackFunction} onSuccessCallback - Callback a ejecutar si la petición es exitosa
 * @param {CallbackFunction} onErrorCallback - Callback a ejecutar si la petición es errónea
 */
export const appFetch = (
    endpoint : string,
    configuration : RequestInit,
    onSuccessCallback : CallbackFunction,
    onErrorCallback : CallbackFunction) : void => {
    const resourceURL = `${BACKEND_URL}${endpoint}`;

    fetch(resourceURL, configuration)
        .then( (response: Response) => _handleResponse(response, onSuccessCallback, onErrorCallback))
        .catch(onNetworkErrorCallback);
}

/** Guarda el JWT del usuario en el navegador */
export const setServiceToken = (serviceToken: string) : void => {
    sessionStorage.setItem(SERVICE_TOKEN_NAME, serviceToken);
}

/** Recupera el JWT del usuario del navegador */
export const getServiceToken = () : string => {
    return sessionStorage.getItem(SERVICE_TOKEN_NAME)!;
}

/** Elimina el JWT del usuario del navegador */
export const removeServiceToken = () : void => {
    sessionStorage.removeItem(SERVICE_TOKEN_NAME);
}

export const setOnReauthenticationCallback = (callback: CallbackFunction) : void => {
    onReauthenticationCallback = callback;
}

export const initializeBackend = (callback: CallbackFunction) : void => {
    onNetworkErrorCallback = callback;
}


/* ****************************************** HANDLERS ****************************************** */
/**
 * Función que maneja la respuesta _response_ recibida del backend.
 * @param {Response} response - Respuesta recibida
 * @param {Function} onSuccessCallback - Función a ejecutar en caso de respuesta exitosa
 * @param {Function} onErrorCallback - Función a ejecutar en caso de error en la respuesta
 * @throws {NetworkErrorException} No se recibió ninguna respuesta
 * @throws {ServiceException} Error procesando respuesta
 */
 const _handleResponse = (response: Response, onSuccessCallback: CallbackFunction, onErrorCallback?: CallbackFunction): void => {
    if (handle2xxResponse(response, onSuccessCallback)) return;
    
    if (handle4xxResponse(response, onErrorCallback)) return;
    
    throw new NetworkErrorException();
};
