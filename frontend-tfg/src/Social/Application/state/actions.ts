import * as actionTypes from './actionTypes';
import {SocialDispatchType} from './actionTypes';
import {Follow} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux, ErrorDto} from "../../../App";
import * as socialService from '../socialService';


/* ************************* DISPATCHABLE ACTIONS ******************** */

export const followUserAction = (follow: Follow) : SocialDispatchType => ({
    type: actionTypes.FOLLOW_USER,
    payload: follow,
})

export const unfollowUserAction = () : SocialDispatchType => ({
    type: actionTypes.FOLLOW_USER,
})

/* ************************* ASYNC ACTIONS ******************** */

export const followUserAsyncAction = (requestorID: string,
                                      targetID: string,
                                      onSuccessCallback: CallbackFunction,
                                      onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (follow: Follow) : void => {
        // Actualiza estado de la aplicación
        dispatch(followUserAction(follow));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(follow);
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
    socialService.followUser(requestorID, targetID, onSuccess, onError);
}

export const unfollowUserAsyncAction = (requestorID: string,
                                      targetID: string,
                                      onSuccessCallback: NoArgsCallbackFunction,
                                      onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = () : void => {
        // Actualiza estado de la aplicación
        dispatch(unfollowUserAction());
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
    socialService.unfollowUser(requestorID, targetID, onSuccess, onError);
}
