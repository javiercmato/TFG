import {appFetch, configFetchParameters} from "../../proxy";

const SOCIAL_ENDPOINT = '/social';
const DEFAULT_PAGE_SIZE = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

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

export const getFollowers = (userID: string,
                             page: number = 0,
                             pageSize: number = DEFAULT_PAGE_SIZE,
                             onSuccessCallback: CallbackFunction,
                             onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = SOCIAL_ENDPOINT + `/followers/${userID}` + '?';
    endpoint += `page=${page}`;
    endpoint += `&pageSize=${pageSize}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const getFollowings = (userID: string,
                              page: number = 0,
                              pageSize: number = DEFAULT_PAGE_SIZE,
                             onSuccessCallback: CallbackFunction,
                             onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    let endpoint = SOCIAL_ENDPOINT + `/followings/${userID}` + '?';
    endpoint += `page=${page}`;
    endpoint += `&pageSize=${pageSize}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}
