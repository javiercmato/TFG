import {ServiceException} from './exceptions';

/**
 * Función que gestiona una respuesta correcta del backend.
 * @param {Response} response - Respuesta recibida
 * @param {Function} onSuccessCallback - Función a ejecutar en caso de respuesta exitosa
 * @returns {boolean} Booleano indicando si esta función es capaz de manejar la respuesta
 * @returns
 */
export const handle2xxResponse = (response: Response, onSuccessCallback: CallbackFunction) : boolean => {
    // Si no se recibe respuesta, no se gestiona
    if (!response) return false;

    // Si el estado de la respuesta no está en el rango [200, 299], no se puede gestionar con este método
    if (response.status < 200 || response.status > 299) return false;

    // SI la respuesta contiene algún JSON en el cuerpo, se ejecuta el callback con su contenido parseado
    if (_isJsonRespone(response))
        response.json()
            .then((json) => onSuccessCallback(json));

    return true;
};

/**
 * Función que gestiona los errores en las respuestas del backend.
 * @param {Response} response - Respuesta recibida
 * @param {Function} onErrorCallback - Función a ejecutar en caso de error en la respuesta
 * @returns {boolean} Booleano indicando si esta función es capaz de manejar la respuesta
 * @throws {ServiceException} Contenido de la respuesta no está en formato JSON
 */
export const handle4xxResponse = (response: Response, onErrorCallback?: CallbackFunction) : boolean => {
    // Si no se recibe respuesta, no se gestiona
    if (!response) return false;

    // Comprobar si el error es ajeno al servidor (no es error del tipo 4xx)
    if (response.status < 400 || response.status > 499) return false;


    // Comprobar si respuesta contiene información del error para procesarlo
    if (!_isJsonRespone(response))
        throw new ServiceException('Empty response body', response.status);

    // Si se recibe callback para gestionar errores se ejecuta con el contenido de la respuesta
    if (onErrorCallback)
        response.json()
            .then((json) => {
                if (json.error || json.globalError)            // Comprobar si respuesta es un ErrorDto
                    onErrorCallback(json);
            });

    return true;
};

/* ************************************ FUNCIONES AUXILIARES ************************************ */
/**
 * Comprueba en las cabeceras de la respuesta si existe contenido y es un JSON
 * @param {Response} response - Respuesta recibida
 * @returns {Boolean} Respuesta contiene un objeto JSON
 */
const _isJsonRespone = (response: Response) : boolean => {
    // eslint-disable-next-line
    // @ts-ignore
    return response.headers
        .get('content-type')
        .includes('application/json');

};
