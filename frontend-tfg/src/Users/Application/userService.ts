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
        onSuccessCallback(authUser);
    };

    // Configurar petición al servicio
    const endpoint = USERS_ENDPOINT + '/register';
    const requestConfig = configFetchParameters('POST', user);


    // Realizar la petición
    appFetch(endpoint, requestConfig, onSuccess, onErrorCallback);
}

