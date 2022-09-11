import * as actionTypes from './actionTypes';
import {SocialDispatchType} from './actionTypes';
import {Follow} from "../../Domain";
import {AppThunk} from "../../../store";
import {appRedux, Block, ErrorDto, Search, SearchCriteria} from "../../../App";
import * as socialService from '../socialService';


/* ************************* DISPATCHABLE ACTIONS ******************** */

export const followUserAction = (follow: Follow) : SocialDispatchType => ({
    type: actionTypes.FOLLOW_USER,
    payload: follow,
})

export const unfollowUserAction = () : SocialDispatchType => ({
    type: actionTypes.UNFOLLOW_USER,
})

export const checkUserFollowsTargetAction = (isFollowing: boolean) : SocialDispatchType => ({
    type: actionTypes.CHECK_USER_FOLLOWS_TARGET,
    payload: isFollowing,
})

export const getFollowersAction = (followersSearch: Search<Follow>) : SocialDispatchType => ({
    type: actionTypes.GET_FOLLOWERS,
    payload: followersSearch,
})

export const getFollowingsAction = (followingsSearch: Search<Follow>) : SocialDispatchType => ({
    type: actionTypes.GET_FOLLOWINGS,
    payload: followingsSearch,
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

export const checkUserFollowsTargetAsyncAction = (requestorID: string,
                                                  targetID: string,
                                                  onSuccessCallback: CallbackFunction,
                                                  onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (response: boolean) : void => {
        // Actualiza estado de la aplicación
        dispatch(checkUserFollowsTargetAction(response));
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
    socialService.checkUserFollowsTarget(requestorID, targetID, onSuccess, onError);
}

export const getFollowersAsyncAction = (criteria: SearchCriteria,
                                        onSuccessCallback: CallbackFunction,
                                        onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (block: Block<Follow>) : void => {
        // Encapsula la respuesta
        const search: Search<Follow> = {
            criteria: criteria,
            result: block
        }
        // Actualiza estado de la aplicación
        dispatch(getFollowersAction(search));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(block);
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
    const {userID, page, pageSize} = criteria;
    socialService.getFollowers(userID!, page, pageSize, onSuccess, onError);
}

export const getFollowingsAsyncAction = (criteria: SearchCriteria,
                                        onSuccessCallback: CallbackFunction,
                                        onErrorCallback: CallbackFunction) : AppThunk => dispatch => {
    const onSuccess: CallbackFunction = (block: Block<Follow>) : void => {
        // Encapsula la respuesta
        const search: Search<Follow> = {
            criteria: criteria,
            result: block
        }
        // Actualiza estado de la aplicación
        dispatch(getFollowingsAction(search));
        dispatch(appRedux.actions.loaded());        // Indica operación ya finalizada

        // Ejecuta el callback recibido con los datos recibidos
        onSuccessCallback(block);
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
    const {userID, page, pageSize} = criteria;
    socialService.getFollowings(userID!, page, pageSize, onSuccess, onError);
}
