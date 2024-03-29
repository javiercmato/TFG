import {appFetch, configFetchParameters} from "../../proxy";
import {
    CreateCategoryParamsDTO,
    CreateCommentParamsDTO,
    CreateRecipeParamsDTO,
    RateRecipeParamsDTO
} from "../Infrastructure";
import {Recipe, RecipeStep} from "../Domain";
import RecipeSummaryDTO from "../Infrastructure/RecipeSummaryDTO";
import {Block} from "../../App";


const RECIPES_ENDPOINT = '/recipes';
const SOCIAL_ENDPOINT = '/social';
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
        // Añade la cabecera a las imágenes para poder mostrarlas correctamente
        recipe.pictures?.forEach((picture) =>
            picture.pictureData = addBase64ImageHeader(picture.pictureData)
        )

        onSuccessCallback(recipe);
    }

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const findRecipes = (name: Nullable<string>,
                            categoryID: Nullable<string>,
                            ingredientsIDLIst: Nullable<Array<string>>,
                            page: number = 0,
                            pageSize: number = DEFAULT_PAGE_SIZE,
                            onSuccessCallback: CallbackFunction,
                            onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = RECIPES_ENDPOINT + '/find' + '?';
    endpoint += `page=${page}`;
    endpoint += `&pageSize=${pageSize}`;
    if (name != null)
        endpoint += `&name=${name}`;
    if (categoryID != null)
        endpoint += `&categoryID=${categoryID}`;
    if (ingredientsIDLIst) {
        endpoint += `&ingredientIdList=`;
        ingredientsIDLIst.forEach((item) => endpoint += item + ',');
    }
    const requestConfig = configFetchParameters('GET');

    // Añade la cabecera a las imágenes para poder mostrarlas correctamente
    let onSuccess = (block: Block<RecipeSummaryDTO>) => {
        block.items.forEach((item: RecipeSummaryDTO) => {
            if (item.picture !== null) {
                item.picture = addBase64ImageHeader(item.picture);
            }
        })

        onSuccessCallback(block);
    }

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const findRecipesByAuthor = (authorID: string,
                                    page: number = 0,
                                    pageSize: number = DEFAULT_PAGE_SIZE,
                                    onSuccessCallback: CallbackFunction,
                                    onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = RECIPES_ENDPOINT + '/findByAuthor' + '?';
    endpoint += `page=${page}`;
    endpoint += `&pageSize=${pageSize}`;
    endpoint += `&authorID=${authorID}`;
    const requestConfig = configFetchParameters('GET');

    // Añade la cabecera a las imágenes para poder mostrarlas correctamente
    let onSuccess = (block: Block<RecipeSummaryDTO>) => {
        block.items.forEach((item: RecipeSummaryDTO) => {
            if (item.picture !== null) {
                item.picture = addBase64ImageHeader(item.picture);
            }
        })

        onSuccessCallback(block);
    }

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const deleteRecipe = (recipeID: string,
                             onSuccessCallback: CallbackFunction,
                             onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = RECIPES_ENDPOINT + `/${recipeID}`;
    const requestConfig = configFetchParameters('DELETE');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const banRecipeAsAdmin = (recipeID: string,
                                 onSuccessCallback: CallbackFunction,
                                 onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = RECIPES_ENDPOINT + `/admin/ban/${recipeID}`;
    const requestConfig = configFetchParameters('PUT');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const getRecipeComments = (recipeID: string,
                                  page: number = 0,
                                  pageSize: number = DEFAULT_PAGE_SIZE,
                                  onSuccessCallback: CallbackFunction,
                                  onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = RECIPES_ENDPOINT + `/comments/${recipeID}` + '?';
    endpoint += `page=${page}`;
    endpoint += `&pageSize=${pageSize}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const addComment = (recipeID: string,
                           params: CreateCommentParamsDTO,
                           onSuccessCallback: CallbackFunction,
                           onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = SOCIAL_ENDPOINT + `/comments/${recipeID}`;
    const requestConfig = configFetchParameters('POST', params);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const banCommentAsAdmin = (commentID: string,
                                  onSuccessCallback: CallbackFunction,
                                  onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = SOCIAL_ENDPOINT + `/comments/admin/ban/${commentID}`;
    const requestConfig = configFetchParameters('PUT');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const rateRecipe = (recipeID: string,
                           params: RateRecipeParamsDTO,
                           onSuccessCallback: CallbackFunction,
                           onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = SOCIAL_ENDPOINT + `/rate/${recipeID}`;
    const requestConfig = configFetchParameters('POST', params);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
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
