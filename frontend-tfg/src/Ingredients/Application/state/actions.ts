import * as actionTypes from './actionTypes';
import {IngredientDispatchType} from './actionTypes';
import * as ingredientService from '../ingredientService';
import {Ingredient, IngredientType} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux as app, Block, ErrorDto, Search, SearchCriteria} from "../../../App";


/* ************************* DISPATCHABLE ACTIONS ******************** */

export const findAllIngredientTypesAction = (types: Array<IngredientType>) : IngredientDispatchType => ({
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

export const findIngredientsAction = (ingredientsSearch: Search<Ingredient>) : IngredientDispatchType => ({
    type: actionTypes.FIND_INGREDIENTS,
    payload: ingredientsSearch
})


export const clearIngredientsSearchAction = () : IngredientDispatchType => ({
    type: actionTypes.CLEAR_INGREDIENTS_SEARCH,
})

export const getMeasureUnitsTypesAction = (measureUnits: Array<string>) : IngredientDispatchType => ({
    type: actionTypes.GET_MEASURE_UNITS,
    payload: measureUnits
})


/* ************************* ASYNC ACTIONS ******************** */

export const findAllIngredientTypesAsyncAction = (onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (types: Array<IngredientType>) : void => {
        // Actualiza estado de la aplicación
        dispatch(findAllIngredientTypesAction(types));
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

export const findAllIngredientsAsyncAction = (criteria: SearchCriteria, onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (block: Block<Ingredient>) : void => {
        // Encapsula la respuesta
        const search: Search<Ingredient> = {
            criteria: criteria,
            result: block
        }

        // Actualiza estado de la aplicación
        dispatch(findAllIngredientsAction(search));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(block);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    const {page, pageSize} = criteria;
    ingredientService.findAllIngredients(page, pageSize, onSuccess, onError);
}

export const findIngredientsAsyncAction = (criteria: SearchCriteria, onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (block: Block<Ingredient>) : void => {
        // Encapsula la respuesta
        const search: Search<Ingredient> = {
            criteria: criteria,
            result: block
        }

        // Actualiza estado de la aplicación
        dispatch(findIngredientsAction(search));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(block);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    const {type, name, page, pageSize} = criteria;
    ingredientService.findIngredients(name, type, page, pageSize, onSuccess, onError);
}

export const getMeasureUnitsAsyncAction = (onSuccessCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (measureUnits: Array<string>) : void => {
        // Actualiza estado de la aplicación
        dispatch(getMeasureUnitsTypesAction(measureUnits));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(measureUnits);
    };

    // Función a ejecutar en caso de error (buscar elementos no produce errores, por eso una función vacía
    const onError = () => {};

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    ingredientService.getMeasureUnits(onSuccess, onError);
}

