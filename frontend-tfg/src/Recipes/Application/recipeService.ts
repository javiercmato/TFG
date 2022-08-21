import {appFetch, configFetchParameters} from "../../proxy";
import {CreateCategoryParamsDTO, CreateRecipeParamsDTO} from "../Infrastructure";

const RECIPES_ENDPOINT = '/recipes';
const DEFAULT_PAGE_SIZE = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);


export const createCategory = (category: CreateCategoryParamsDTO,
                               onSuccessCallback: CallbackFunction,
                               onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = RECIPES_ENDPOINT + '/categories';
    const requestConfig = configFetchParameters('POST', category);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const getCategories = (onSuccessCallback: CallbackFunction,
                              onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = RECIPES_ENDPOINT + '/categories';
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const createRecipe = (recipe: CreateRecipeParamsDTO,
                             onSuccessCallback: CallbackFunction,
                             onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = RECIPES_ENDPOINT + '/';
    const requestConfig = configFetchParameters('POST', recipe);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const getRecipeDetails = (recipeID: string,
                                 onSuccessCallback: CallbackFunction,
                                 onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = RECIPES_ENDPOINT + `/${recipeID}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}
