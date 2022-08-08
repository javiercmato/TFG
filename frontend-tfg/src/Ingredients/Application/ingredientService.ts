import {appFetch, configFetchParameters} from "../../proxy";


const INGREDIENTS_ENDPOINT = '/ingredients';
const DEFAULT_PAGE_SIZE = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

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

export const createIngredient = (name: string,
                                 ingredientTypeID: string,
                                 userID: string,
                                 onSuccessCallback: CallbackFunction,
                                 onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = INGREDIENTS_ENDPOINT + '/';
    const body = {
        name,
        ingredientTypeID,
        creatorID: userID
    }
    const requestConfig = configFetchParameters('POST', body);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}


export const findAllIngredients = (onSuccessCallback: CallbackFunction,
                                   onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = INGREDIENTS_ENDPOINT + '/';
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const findIngredientsByType = (typeID: string,
                                      page: number = 0,
                                      pageSize: number = DEFAULT_PAGE_SIZE,
                                      onSuccessCallback: CallbackFunction,
                                      onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = INGREDIENTS_ENDPOINT + '/find' + '?';
    endpoint += `page=${page}`;
    endpoint += `pageSize=${pageSize}`;
    endpoint += `typeID=${typeID}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const findIngredientsByName = (name: string,
                                      page: number = 0,
                                      pageSize: number = DEFAULT_PAGE_SIZE,
                                      onSuccessCallback: CallbackFunction,
                                      onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = INGREDIENTS_ENDPOINT + '/find' + '?';
    endpoint += `page=${page}`;
    endpoint += `pageSize=${pageSize}`;
    endpoint += `name=${name}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}
