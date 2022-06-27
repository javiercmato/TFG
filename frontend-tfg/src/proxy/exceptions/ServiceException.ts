class ServiceException extends Error {
    /**
     * @type {String} Texto descriptivo del error
     */
    _details: string;

    /**
     * @type {number} Código de estado asignado al error
     */
    _statusCode: number;


    constructor(details: string, statusCode: number) {
        super(`${ServiceException.name} [${statusCode}]: ${details}`);
        this._details = details;
        this._statusCode = statusCode;
    }


    get details(): string {
        return this._details;
    }

    get statusCode(): number {
        return this._statusCode;
    }
}

export default ServiceException;
