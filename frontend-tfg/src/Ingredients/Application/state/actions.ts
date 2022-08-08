import * as actionTypes from './actionTypes';
import {IngredientDispatchType} from './actionTypes';
import * as ingredientService from '../ingredientService';
import {Ingredient, IngredientType} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux as app, Block, ErrorDto, Search, SearchCriteria} from "../../../App";


/* ************************* DISPATCHABLE ACTIONS ******************** */

export const findAllIngredientTypes = (types: Array<IngredientType>) : IngredientDispatchType => ({
    type: actionTypes.FIND_ALL_INGREDIENT_TYPES,
    payload: types
})

export const createIngredientTypeAction = (ingredientType: IngredientType) : IngredientDispatchType => ({
    type: actionTypes.CREATE_INGREDIENT_TYPE,
    payload: ingredientType
})

export const createIngredientAction = (ingredient: Ingredient) : IngredientDispatchType => ({
    type: actionTypes.CREATE_INGREDIENT_TYPE,
    payload: ingredient
})

export const findAllIngredientsAction = (ingredientsSearch: Search<Ingredient>) : IngredientDispatchType => ({
    type: actionTypes.FIND_ALL_INGREDIENTS,
    payload: ingredientsSearch
})

export const findIngredientsByTypeAction = (ingredientsSearch: Search<Ingredient>) : IngredientDispatchType => ({
    type: actionTypes.FIND_INGREDIENTS_BY_TYPE,
    payload: ingredientsSearch
})

export const findIngredientsByNameAction = (ingredientsSearch: Search<Ingredient>) : IngredientDispatchType => ({
    type: actionTypes.FIND_INGREDIENTS_BY_NAME,
    payload: ingredientsSearch
})


/* ************************* ASYNC ACTIONS ******************** */

export const findAllIngredientTypesAsyncAction = (onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (types: Array<IngredientType>) : void => {
        // Actualiza estado de la aplicación
        dispatch(findAllIngredientTypes(types));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(types);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    ingredientService.getIngredientTypes(onSuccess, onError);
}

export const createIngredientTypeAsyncAction = (name: string,
                                                onSuccessCallback: CallbackFunction,
                                                onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (ingredientType: IngredientType) : void => {
        // Actualiza estado de la aplicación
        dispatch(createIngredientTypeAction(ingredientType));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(ingredientType);
    };

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(app.actions.error(error));
        dispatch(app.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    ingredientService.createIngredientType(name, onSuccess, onError);
}


export const createIngredientAsyncAction = (name: string,
                                            ingredientTypeID: string,
                                            userID: string,
                                            onSuccessCallback: CallbackFunction,
                                            onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (ingredient: Ingredient) : void => {
        // Actualiza estado de la aplicación
        dispatch(createIngredientAction(ingredient));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(ingredient);
    };

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(app.actions.error(error));
        dispatch(app.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    }

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    ingredientService.createIngredient(name, ingredientTypeID, userID, onSuccess, onError);
}

export const findAllIngredientsAsyncAction = (onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (types: Array<IngredientType>) : void => {
        // Actualiza estado de la aplicación
        dispatch(findAllIngredientTypes(types));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(types);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    ingredientService.findAllIngredients(onSuccess, onError);
}

export const findIngredientsByNameAsyncAction = (criteria: SearchCriteria, onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (block: Block<Ingredient>) : void => {
        // Encapsula la respuesta
        const search: Search<Ingredient> = {
            criteria: criteria,
            result: block
        }

        // Actualiza estado de la aplicación
        dispatch(findIngredientsByNameAction(search));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(block);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    const {name, page, pageSize} = criteria;
    ingredientService.findIngredientsByName(name!, page, pageSize, onSuccess, onError);
}

export const findIngredientsByTypeAsyncAction = (criteria: SearchCriteria, onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (block: Block<Ingredient>) : void => {
        // Encapsula la respuesta
        const search: Search<Ingredient> = {
            criteria: criteria,
            result: block
        }

        // Actualiza estado de la aplicación
        dispatch(findIngredientsByTypeAction(search));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(block);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    const {type, page, pageSize} = criteria;
    ingredientService.findIngredientsByType(type!, page, pageSize, onSuccess, onError);
}
