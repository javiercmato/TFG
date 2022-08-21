import {appFetch, configFetchParameters} from "../../proxy";
import {CreateCategoryParamsDTO, CreateRecipeParamsDTO} from "../Infrastructure";
import {Recipe, RecipeStep} from "../Domain";

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

    // Limpia los datos de la receta para poder mostrarlos correctamente
    let onSuccess = (recipe: Recipe) => {
        // Ordena los pasos de la receta antes de guardarlos en redux
        recipe.steps = sortRecipeSteps(recipe.steps);
        recipe.pictures?.forEach((picture) =>
            picture.pictureData = addBase64ImageHeader(picture.pictureData)
        )

        onSuccessCallback(recipe);
    }

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

/* ************************* FUNCIONES AUXILIARES ************************* */
const sortRecipeSteps = (steps: Array<RecipeStep>): Array<RecipeStep> => {
    // compareFunction(a,b) < 0 : a va antes que b
    // compareFunction(a,b) > 0 : b va antes que a
    const compareFunction = (a: RecipeStep, b: RecipeStep) => a.step - b.step;
    return steps.sort(compareFunction);
}

const addBase64ImageHeader = (imageB64StringData: string) : string => {
    return "data:image/*;base64," + imageB64StringData;
}
