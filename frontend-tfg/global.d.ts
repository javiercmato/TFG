export {};

/* Tipos de datos que queremos hacer accesibles en toda la aplicación */
declare global {
    /** Callback que no recibe ni devuelve nada */
    type NoArgsCallbackFunction = () => void;

    /** Callback que recibe argumentos y no devuelve nada */
    type CallbackFunction = (args: any) => void;

    /** Tipo que admite un dato del tipo <T> o nulo */
     type Nullable<T> = T | null;
}
