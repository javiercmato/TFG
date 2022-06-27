import {appFetch, configFetchParameters, setOnReauthenticationCallback, setServiceToken} from "../../proxy";
import {AuthenticatedUser, User} from "../Domain";

const USERS_ENDPOINT = '/users';

export const signUp = (user: User,
                       onSuccessCallback: CallbackFunction,
                       onErrorCallback: CallbackFunction,
                       onReauthenticationCallback: CallbackFunction) : void => {
    // Callback para cuando se registra con éxito al usuario
    let onSuccess = (authUser: AuthenticatedUser) : void => {
        setServiceToken(authUser.serviceToken);     // Guardar JWT en navegador
        setOnReauthenticationCallback(onReauthenticationCallback);
        authUser.user = formatUserData(authUser.user);              // Formatear datos del usuario
        onSuccessCallback(authUser);
    };

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + '/register';
    const requestConfig = configFetchParameters('POST', user);


    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}


/** Función que limpia y formatea los datos del usuario recibido */
const formatUserData = (user: User) : User => {
    // Añadir cabecera de Base64 al avatar del usuario para visualizarlo correctamente
    //https://stackoverflow.com/a/40196009/11295728
    user.avatar = "data:image/*;base64," + user.avatar;

    return user;
}

