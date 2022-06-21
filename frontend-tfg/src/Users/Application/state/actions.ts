import * as actionTypes from './actionTypes';
import {UserDispatchType} from './actionTypes';
import {AuthenticatedUser, User} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux as app} from "../../../App";
import * as userService from "../userService";

/* ************************* DISPATCHABLE ACTIONS ******************** */

export const signUp = (authenticatedUser: AuthenticatedUser) : UserDispatchType => ({
    type: actionTypes.SIGN_UP,
    payload: authenticatedUser
});




/* ************************* ASYNC ACTIONS ******************** */
export const signUpAsyncAction = (
    user: User, onSuccessCallback: CallbackFunction, onErrorCallback: CallbackFunction, onReauthenticateCallback: CallbackFunction): AppThunk => dispatch => {
    // Función a ejecutar en caso de éxito
    const onSuccess: CallbackFunction = (authUser: AuthenticatedUser) : void => {
        // Actualiza estado de la aplicación
        dispatch(signUp(authUser));
        dispatch(app.actions.loaded());         // Indica operación ya finalizada

        // Ejecuta el callback recibido con el usuario recuperado
        onSuccessCallback(authUser);
    };

    // Indicar que se está realizando una operación
    dispatch(app.actions.loading());

    // Llamar al servicio y ejecutar los callbacks
    userService.signUp(user, onSuccess, onErrorCallback, onReauthenticateCallback);
}
