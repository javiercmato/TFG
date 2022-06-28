import * as actionTypes from './actionTypes';
import {UserDispatchType} from './actionTypes';
import {AuthenticatedUser, User} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux as app, ErrorDto} from "../../../App";
import * as userService from "../userService";

/* ************************* DISPATCHABLE ACTIONS ******************** */

export const signUpAction = (authenticatedUser: AuthenticatedUser) : UserDispatchType => ({
    type: actionTypes.SIGN_UP,
    payload: authenticatedUser
});

export const loginAction = (authenticatedUser: AuthenticatedUser) : UserDispatchType => ({
    type: actionTypes.LOGIN,
    payload: authenticatedUser
})


/* ************************* ASYNC ACTIONS ******************** */
export const signUpAsyncAction = (
    user: User,
    onSuccessCallback: CallbackFunction,
    onErrorCallback: CallbackFunction,
    onReauthenticateCallback: CallbackFunction): AppThunk => dispatch => {

    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (authUser: AuthenticatedUser) : void => {
        // Actualiza estado de la aplicación
        dispatch(signUpAction(authUser));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(authUser);
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
    userService.signUp(user, onSuccess, onError, onReauthenticateCallback);
}


export const loginAsyncAction = (
    nickname: string,
    password: string,
    onSuccessCallback: CallbackFunction,
    onErrorCallback: CallbackFunction,
    onReauthenticateCallback: CallbackFunction) : AppThunk => dispatch => {

    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (authUser: AuthenticatedUser) : void => {
    // Actualiza estado de la aplicación
    dispatch(loginAction(authUser));
    dispatch(app.actions.loaded());         // Indica operación ya finalizada

    // Ejecuta el callback recibido con el usuario recuperado
    onSuccessCallback(authUser);
    };

    // Función a ejecutar en caso de error
    const onError: CallbackFunction = (error: ErrorDto): void => {
        // Actualiza estado de la aplicación
        dispatch(app.actions.error(error));
        dispatch(app.actions.loaded());

        // Ejecuta el callback recibido
        onErrorCallback(error);
    };

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    userService.login(nickname, password, onSuccess, onError, onReauthenticateCallback);
}

