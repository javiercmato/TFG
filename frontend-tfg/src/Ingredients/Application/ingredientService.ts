import {appFetch, configFetchParameters} from "../../proxy";


const INGREDIENTS_ENDPOINT = '/ingredients';

export const getIngredientTypes = (onSuccessCallback: CallbackFunction,
                                   onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = INGREDIENTS_ENDPOINT + '/types';
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const createIngredientType = (name: string,
                                     onSuccessCallback: CallbackFunction,
                                     onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = INGREDIENTS_ENDPOINT + '/types';
    const body = {
        name: name
    }
    const requestConfig = configFetchParameters('POST', body);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}
