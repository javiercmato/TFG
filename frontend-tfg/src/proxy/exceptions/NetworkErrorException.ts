import translations from "../../i18n/messages";

const errorMessage = translations.es["proxy.exceptions.NetworkErrorException"];

class NetworkErrorException extends Error {

    constructor() {
        super(errorMessage);
    }
}

export default NetworkErrorException;
