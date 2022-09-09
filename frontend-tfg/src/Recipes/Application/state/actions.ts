import * as actionTypes from './actionTypes';
import {RecipeDispatchType} from './actionTypes';
import * as recipeService from '../recipeService';
import {Category, Recipe} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux as app, appRedux, Block, ErrorDto, Search, SearchCriteria} from "../../../App";
import {
    CreateCategoryParamsDTO,
    CreateCommentParamsDTO,
    CreateRecipeParamsDTO,
    RecipeSummaryDTO
} from "../../Infrastructure";
import {Comment} from "../../../Social";


/* ************************* DISPATCHABLE ACTIONS ******************** */

export const createCategoryAction = (category: Category) : RecipeDispatchType => ({
    type: actionTypes.CREATE_CATEGORY,
    payload: category,
})

export const getCategoriesAction = (category: Array<Category>) : RecipeDispatchType => ({
    type: actionTypes.GET_CATEGORIES,
    payload: category,
})

export const createRecipeAction = (recipe: Recipe) : RecipeDispatchType => ({
    type: actionTypes.CREATE_CATEGORY,
    payload: recipe,
})

export const getRecipeDetailsAction = (recipe: Recipe) : RecipeDispatchType => ({
    type: actionTypes.GET_RECIPE_DETAILS,
    payload: recipe,
})

export const clearRecipeDetailsAction = () : RecipeDispatchType => ({
    type: actionTypes.CLEAR_RECIPE_DETAILS,
})

export const findRecipesAction = (recipesSearch: Search<RecipeSummaryDTO>) : RecipeDispatchType => ({
    type: actionTypes.FIND_RECIPES,
    payload: recipesSearch,
})

export const clearRecipesSearchAction = () : RecipeDispatchType => ({
    type: actionTypes.CLEAR_RECIPES_SEARCH,
})

export const deleteRecipeAction = () : RecipeDispatchType => ({
    type: actionTypes.DELETE_RECIPE,
})

export const banRecipeAction = (isBanned: boolean) : RecipeDispatchType => ({
    type: actionTypes.BAN_RECIPE,
    payload: isBanned,
})

export const getRecipeCommentsAction = (commentsSearch: Search<Comment>) : RecipeDispatchType => ({
    type: actionTypes.GET_RECIPE_COMMENTS,
    payload: commentsSearch,
})

export const addCommentAction = (comment: Comment) : RecipeDispatchType => ({
    type: actionTypes.ADD_COMMENT,
    payload: comment,
})

/* ************************* ASYNC ACTIONS ******************** */

export const createCategoryAsyncAction = (category: CreateCategoryParamsDTO,
                                          onSuccessCallback: CallbackFunction,
                                          onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (response: Category) : void => {
        // Actualiza estado de la aplicación
        dispatch(createCategoryAction(response));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(response);
    }

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(appRedux.actions.error(error));
        dispatch(appRedux.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(appRedux.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    recipeService.createCategory(category, onSuccess, onError);
}

export const getCategoriesAsyncAction = (onSuccessCallback: CallbackFunction,
                                         onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (categories: Array<Category>) : void => {
        // Actualiza estado de la aplicación
        dispatch(getCategoriesAction(categories));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(categories);
    }

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(appRedux.actions.error(error));
        dispatch(appRedux.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(appRedux.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    recipeService.getCategories(onSuccess, onError);
}

export const createRecipeAsyncAction = (recipe: CreateRecipeParamsDTO,
                                        onSuccessCallback: CallbackFunction,
                                        onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (response: Recipe) : void => {
        // Actualiza estado de la aplicación
        dispatch(createRecipeAction(response));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(response);
    }

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(appRedux.actions.error(error));
        dispatch(appRedux.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(appRedux.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    recipeService.createRecipe(recipe, onSuccess, onError);
}

export const getRecipeDetailsAsyncAction = (recipeID: string,
                                            onSuccessCallback: CallbackFunction,
                                            onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (recipe: Recipe) : void => {
        // Actualiza estado de la aplicación
        dispatch(getRecipeDetailsAction(recipe));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(recipe);
    }

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(appRedux.actions.error(error));
        dispatch(appRedux.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(appRedux.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    recipeService.getRecipeDetails(recipeID, onSuccess, onError);
}

export const findRecipesAsyncAction = (criteria: SearchCriteria, onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (block: Block<RecipeSummaryDTO>) : void => {
        // Encapsula la respuesta
        const search: Search<RecipeSummaryDTO> = {
            criteria: criteria,
            result: block
        }

        // Actualiza estado de la aplicación
        dispatch(findRecipesAction(search));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(block);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    const {name, category, ingredients, page, pageSize} = criteria;
    recipeService.findRecipes(name, category, ingredients, page, pageSize, onSuccess, onError);
}

export const deleteRecipeAsyncAction = (recipeID: string,
                                        onSuccessCallback: NoArgsCallbackFunction,
                                        onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: NoArgsCallbackFunction = () : void => {
        // Actualiza estado de la aplicación
        dispatch(deleteRecipeAction());
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback();
    }

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(appRedux.actions.error(error));
        dispatch(appRedux.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(appRedux.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    recipeService.deleteRecipe(recipeID, onSuccess, onError);
}

export const banRecipeAsyncAction = (recipeID: string,
                                     onSuccessCallback: NoArgsCallbackFunction,
                                     onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (isBanned: boolean) : void => {
        // Actualiza estado de la aplicación
        dispatch(banRecipeAction(isBanned));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback();
    }

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(appRedux.actions.error(error));
        dispatch(appRedux.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(appRedux.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    recipeService.banRecipeAsAdmin(recipeID, onSuccess, onError);
}

export const getRecipeCommentsAsyncAction = (criteria: SearchCriteria, onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (block: Block<Comment>) : void => {
        // Encapsula la respuesta
        const search: Search<Comment> = {
            criteria: criteria,
            result: block
        }

        // Actualiza estado de la aplicación
        dispatch(getRecipeCommentsAction(search));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(block);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    const {recipeID, page, pageSize} = criteria;
    recipeService.getRecipeComments(recipeID!, page, pageSize, onSuccess, onError);
}

export const addCommentAsyncAction = (recipeID: string,
                                      params: CreateCommentParamsDTO,
                                      onSuccessCallback: CallbackFunction,
                                      onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (response: Comment) : void => {
        // Actualiza estado de la aplicación
        dispatch(addCommentAction(response));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(response);
    }

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(appRedux.actions.error(error));
        dispatch(appRedux.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(appRedux.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    recipeService.addComment(recipeID, params, onSuccess, onError);
}
