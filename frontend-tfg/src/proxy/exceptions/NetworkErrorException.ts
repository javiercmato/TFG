class NetworkErrorException extends Error {
    /**
     * @type {String} Texto descriptivo del error
     */
    _details: string;


    constructor(details: string) {
        super(`${NetworkErrorException.name}: ${details}`);
        this._details = details;
    }


    get details(): string {
        return this._details;
    }
}

export default NetworkErrorException;
