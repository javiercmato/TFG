import {IngredientType} from "../Domain";
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

export const createIngredientType = (ingredientType: IngredientType,
                                     onSuccessCallback: CallbackFunction,
                                     onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = INGREDIENTS_ENDPOINT + '/types';
    const requestConfig = configFetchParameters('POST', ingredientType);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}
