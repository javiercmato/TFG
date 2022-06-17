import {CallbackFunction} from "./types";
import {NetworkException} from "./exceptions";
import {handle2xxResponse, handle4xxResponse} from './responseHandlers';

/** URL del backend extraído de la configuración del entorno */
const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;

/* ****************************************** SERVICIOS ***************************************** */

/**
 * Configura la petición que se realizará al backend.
 * @param {String} method - Tipo de petición HTTP: {GET, POST, PUT, etc}
 * @param {Object} body - Contenido a enviar en el cuerpo de la petición
 * @returns {Object} - Configuración para realizar la petición
 */
export const configFetchParameters = (method: string, body?: Record<string, any>): RequestInit => {
    const configuration : RequestInit = {};

    // Establecer tipo de petición HTTP
    if (method)
        configuration.method = method;

    if (body) {
        // Si contenido es un formulario, lo envía sin modificarlo
        if (body instanceof FormData)
            configuration.body = body;
        // Sino, convierte el contenido a JSON
        else {
            configuration.headers = {
                'Content-Type' : 'application/json'
            };
            configuration.body = JSON.stringify(body);
        }
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
        .then( (response: Response) => _handleResponse(response, onSuccessCallback, onErrorCallback));
}


/* ****************************************** HANDLERS ****************************************** */
/**
 * Función que maneja la respuesta _response_ recibida del backend.
 * @param {Response} response - Respuesta recibida
 * @param {Function} onSuccessCallback - Función a ejecutar en caso de respuesta exitosa
 * @param {Function} onErrorCallback - Función a ejecutar en caso de error en la respuesta
 * @throws {NetworkException} No se recibió ninguna respuesta
 * @throws {ServiceException} Error procesando respuesta
 */
 const _handleResponse = (response: Response, onSuccessCallback: CallbackFunction, onErrorCallback?: CallbackFunction): void => {
    if (handle2xxResponse(response, onSuccessCallback)) return;
    
    if (handle4xxResponse(response, onErrorCallback)) return;
    
    throw new NetworkException(response.statusText);
};
