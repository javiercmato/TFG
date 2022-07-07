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

export const logoutAction = () : UserDispatchType => ({
    type: actionTypes.LOGOUT
})

export const changePasswordAction = () : UserDispatchType => ({
    type: actionTypes.LOGOUT
})

export const getUserProfileAction = (user: User) : UserDispatchType => ({
    type: actionTypes.GET_USER_PROFILE,
    payload: user,
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

export const loginWithServiceTokenAsyncAction = (
    onReauthenticateCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (authUser: AuthenticatedUser) : void => {
        // Actualiza estado de la aplicación
        if (authUser)
            dispatch(loginAction(authUser));
    };
    // Función a ejecutar en caso de no poder autenticarse
    const onReauthenticate: NoArgsCallbackFunction = () : void => {
        // Ejecuta el callback recibido
        // @ts-ignore
        onReauthenticateCallback();
    }

    // Llamar al servicio y ejecutar los callbacks
    userService.loginWithServiceToken(onSuccess, onReauthenticate);
}

export const logoutAsyncAction = () : AppThunk => dispatch => {
    dispatch(logoutAction());

    // Llamar al servicio y ejecutar los callbacks
    userService.logout();
}

export const changePasswordAsyncAction = (userID: string,
    oldPassword: string,
    newPassword: string,
    onSuccessCallback: CallbackFunction,
    onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    dispatch(changePasswordAction());

    // LLamar al servicio y ejecutar los callbacks
    userService.changePassword(userID, oldPassword, newPassword, onSuccessCallback, onErrorCallback);
}

export const getUserProfileAsyncAction = (userID: string,
    onSuccessCallback: CallbackFunction,
    onErrorCallback: CallbackFunction) : AppThunk => dispatch => {

    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (user: User) : void => {
        // Actualiza estado de la aplicación
        dispatch(getUserProfileAction(user));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(user);
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
    userService.getUserProfile(userID, onSuccess, onError);
}
