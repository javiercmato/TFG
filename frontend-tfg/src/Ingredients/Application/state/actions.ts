import * as actionTypes from './actionTypes';
import {IngredientDispatchType} from './actionTypes';
import * as ingredientService from '../ingredientService';
import {IngredientType} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux as app, ErrorDto} from "../../../App";


/* ************************* DISPATCHABLE ACTIONS ******************** */

export const findAllIngredientTypes = (types: Array<IngredientType>) : IngredientDispatchType => ({
    type: actionTypes.FIND_ALL_INGREDIENT_TYPES,
    payload: types
})

export const createIngredientTypeAction = (ingredientType: IngredientType) : IngredientDispatchType => ({
    type: actionTypes.CREATE_INGREDIENT_TYPE,
    payload: ingredientType
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
