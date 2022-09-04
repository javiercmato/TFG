import {
    appFetch,
    configFetchParameters,
    getServiceToken,
    removeServiceToken,
    setOnReauthenticationCallback,
    setServiceToken
} from "../../proxy";
import {AuthenticatedUser, PrivateList, User} from "../Domain";
import {CreatePrivateListParamsDTO} from "../Infrastructure";
import {RecipeSummaryDTO} from "../../Recipes";

const USERS_ENDPOINT = '/users';

export const signUp = (user: User,
                       onSuccessCallback: CallbackFunction,
                       onErrorCallback: CallbackFunction,
                       onReauthenticationCallback: NoArgsCallbackFunction) : void => {
    // Callback para cuando se registra con éxito al usuario
    let onSuccess = (authUser: AuthenticatedUser) : void => {
        processAuthenticatedUser(authUser, onSuccessCallback, onReauthenticationCallback)
    };

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + '/register';
    const requestConfig = configFetchParameters('POST', user);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}


export const login = (nickname: string,
                      password: string,
                      onSuccessCallback: CallbackFunction,
                      onErrorCallback: CallbackFunction,
                      onReauthenticationCallback: NoArgsCallbackFunction) : void => {
    // Callback para cuando se inicia sesión con éxito
    let onSuccess = (authUser: AuthenticatedUser) : void => {
        processAuthenticatedUser(authUser, onSuccessCallback, onReauthenticationCallback);
    };

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + '/login';
    const loginParams = {'nickname': nickname, 'password': password};
    const requestConfig = configFetchParameters('POST', loginParams);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const loginWithServiceToken = (onSuccessCallback: CallbackFunction,
                                      onReauthenticateCallback: NoArgsCallbackFunction) : void => {
    const serviceToken: string = getServiceToken();
    // Si hay token, se ejecuta el callback de éxito
    if (!serviceToken) {
        // @ts-ignore
        onSuccessCallback();
        return;
    }

    setOnReauthenticationCallback(onReauthenticateCallback);

    // Callback para cuando se inicia sesión con éxito
    let onSuccess = (authUser: AuthenticatedUser) : void => {
        onSuccessCallback(authUser);
    };
    // Callback para cuando se proudce error
    let onError: NoArgsCallbackFunction = () => removeServiceToken();

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + '/login/token';
    const requestConfig = configFetchParameters('POST');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onError);
}

export const logout = () : void => {
    removeServiceToken();
}

export const changePassword = (userID: string,
                               oldPassword: string,
                               newPassword: string,
                               onSuccessCallback: CallbackFunction,
                               onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}/changePassword`;
    const changePasswordParams = {oldPassword, newPassword};
    const requestConfig = configFetchParameters('PUT', changePasswordParams);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const findUserByNickname = (nickname: string,
                                   onSuccessCallback: CallbackFunction,
                                   onErrorCallback: CallbackFunction): void => {
    // Callback para cuando se encuentra al usuario con éxito
    let onSuccess = (user: User) : void => {
        user = formatUserData(user);
        onSuccessCallback(user);
    };

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/?nickname=${nickname}`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const updateProfile = (userID: string,
                              updatedUser: User,
                              onSuccessCallback: CallbackFunction,
                              onErrorCallback: CallbackFunction) : void => {
    // Callback para cuando se actualiza al usuario con éxito
    let onSuccess = (user: User) : void => {
        user = formatUserData(user);
        onSuccessCallback(user);
    };

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}`;
    const requestConfig = configFetchParameters('PUT', updatedUser);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const banUser = (targetUserID: string,
                        onSuccessCallback: CallbackFunction,
                        onErrorCallback: CallbackFunction) : void => {
    // Callback para cuando se banea al usuario con éxito
    let onSuccess = (isBanned: boolean) : void => {
        onSuccessCallback(isBanned);
    };

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/admin/ban/${targetUserID}`;
    const requestConfig = configFetchParameters('PUT');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const deleteUser = (userID: string,
                           onSuccessCallback: NoArgsCallbackFunction,
                           onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}`;
    const requestConfig = configFetchParameters('DELETE');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const createPrivateList = (userID: string,
                                  params: CreatePrivateListParamsDTO,
                                  onSuccessCallback: CallbackFunction,
                                  onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}/lists`;
    const requestConfig = configFetchParameters('POST', params);

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const getPrivateLists = (userID: string,
                                onSuccessCallback: CallbackFunction,
                                onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}/lists`;
    const requestConfig = configFetchParameters('GET');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const getPrivateListDetails = (userID: string,
                                      privateListID: string,
                                      onSuccessCallback: CallbackFunction,
                                      onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}/lists/${privateListID}`;
    const requestConfig = configFetchParameters('GET');

    // Añade la cabecera a las imágenes para poder mostrarlas correctamente
    let onSuccess = (privateList: PrivateList) => {
        privateList.recipes.forEach((dto: RecipeSummaryDTO) => {

            if (dto.picture !== null)
                dto.picture = addBase64ImageHeader(dto.picture);
        })

        onSuccessCallback(privateList);
    }

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

export const addRecipeToPrivateList = (userID: string,
                                       privateListID: string,
                                       recipeID: string,
                                       onSuccessCallback: CallbackFunction,
                                       onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}/lists/${privateListID}/add/${recipeID}`;
    const requestConfig = configFetchParameters('POST');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const removeRecipeFromPrivateList = (userID: string,
                                            privateListID: string,
                                            recipeID: string,
                                            onSuccessCallback: CallbackFunction,
                                            onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}/lists/${privateListID}/remove/${recipeID}`;
    const requestConfig = configFetchParameters('DELETE');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}

export const deletePrivateList = (userID: string,
                                  privateListID: string,
                                  onSuccessCallback: CallbackFunction,
                                  onErrorCallback: CallbackFunction) : void => {
    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + `/${userID}/lists/${privateListID}`;
    const requestConfig = configFetchParameters('DELETE');

    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccessCallback, onErrorCallback);
}


/* ************************* FUNCIONES AUXILIARES ************************* */
/** Guarda el JWT en el navegador, da formato al usuario y asigna los callbacks */
const processAuthenticatedUser = (authUser: AuthenticatedUser,
                                  onSuccessCallback: CallbackFunction,
                                  onReauthenticateCallback: NoArgsCallbackFunction) : void => {
    setServiceToken(authUser.serviceToken);
    setOnReauthenticationCallback(onReauthenticateCallback);
    authUser.user = formatUserData(authUser.user);
    onSuccessCallback(authUser);
}

/** Función que limpia y formatea los datos del usuario recibido */
const formatUserData = (user: User) : User => {
    // Añadir cabecera de Base64 al avatar del usuario para visualizarlo correctamente
    //https://stackoverflow.com/a/40196009/11295728
    if (user?.avatar) {
        user.avatar = addBase64ImageHeader(user.avatar);
    }

    return user;
}

const addBase64ImageHeader = (imageB64StringData: string) : string => {
    return "data:image/*;base64," + imageB64StringData;
}
