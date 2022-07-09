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
    type: actionTypes.CHANGE_PASSWORD
})

export const findUserByNicknameAction = (user: User) : UserDispatchType => ({
    type: actionTypes.FIND_USER_BY_NICKNAME,
    payload: user,
})

export const updateProfileAction = (user: User) : UserDispatchType => ({
    type: actionTypes.UPDATE_PROFILE,
    payload: user,
})

export const banUserAction = (isBanned: boolean) : UserDispatchType => ({
    type: actionTypes.BAN_USER,
    payload: isBanned,
})


/* ************************* ASYNC ACTIONS ******************** */
export const signUpAsyncAction = (
    user: User,
    onSuccessCallback: CallbackFunction,
    onErrorCallback: CallbackFunction,
    onReauthenticateCallback: NoArgsCallbackFunction): AppThunk => dispatch => {

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
    onReauthenticateCallback: NoArgsCallbackFunction) : AppThunk => dispatch => {

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
    onReauthenticateCallback: NoArgsCallbackFunction): AppThunk => dispatch => {
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

export const findUserByNicknameAsyncAction = (nickname: string,
                                              onSuccessCallback: CallbackFunction,
                                              onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (user: User) : void => {
        // Actualiza estado de la aplicación
        dispatch(findUserByNicknameAction(user));
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
    userService.findUserByNickname(nickname, onSuccess, onError);
}

export const updateProfileAsyncAction = (userID: string,
                                         updatedUser: User,
                                         onSuccessCallback: CallbackFunction,
                                         onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (user: User) : void => {
        // Actualiza estado de la aplicación
        dispatch(updateProfileAction(user));
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
    }

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    userService.updateProfile(userID, updatedUser, onSuccess, onError);
}

export const banUserAsyncAction = (targetUserID: string,
                                   onSuccessCallback: CallbackFunction,
                                   onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (isBanned: boolean) : void => {
        // Actualiza estado de aplicación
        dispatch(banUserAction(isBanned));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(isBanned);
    }

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
    userService.banUser(targetUserID, onSuccess, onError);
}
