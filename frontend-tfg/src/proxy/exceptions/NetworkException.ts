class NetworkException extends Error {
    /**
     * @type {String} Texto descriptivo del error
     */
    _details: string;


    constructor(details: string) {
        super(`${NetworkException.name}: ${details}`);
        this._details = details;
    }


    get details(): string {
        return this._details;
    }
}

export default NetworkException;
