import {appFetch, configFetchParameters} from "../../proxy";

const SOCIAL_ENDPOINT = '/social';

export const followUser = (requestorID: string,
                           targetID: string,
                           onSuccessCallback: CallbackFunction,
                           onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = SOCIAL_ENDPOINT + `/follow/${requestorID}/${targetID}`;
    const requestConfig = configFetchParameters('PUT');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const unfollowUser = (requestorID: string,
                             targetID: string,
                             onSuccessCallback: CallbackFunction,
                             onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = SOCIAL_ENDPOINT + `/unfollow/${requestorID}/${targetID}`;
    const requestConfig = configFetchParameters('DELETE');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const checkUserFollowsTarget = (requestorID: string,
                                      targetID: string,
                                      onSuccessCallback: CallbackFunction,
                                      onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = SOCIAL_ENDPOINT + `/followings/${requestorID}/check?`;
    endpoint += `targetID=${targetID}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}
