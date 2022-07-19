import {User} from "../../Domain";


/** Tipo de estado para información asociada a los usuarios */
interface IUserState {
    user: Nullable<User>,               // Datos del usuario autenticado
    userSearch: Nullable<User>,         // Datos de la búsqueda de un usuario
}

const initialState : IUserState = {
    user: null,
    userSearch: null,
}


export {initialState};
export type {IUserState};
