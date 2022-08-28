import {User} from "../../Domain";
import PrivateList from "../../Domain/PrivateList";


/** Tipo de estado para información asociada a los usuarios */
interface IUserState {
    user: Nullable<User>,               // Datos del usuario autenticado
    userSearch: Nullable<User>,         // Datos de la búsqueda de un usuario
    privateLists: Array<PrivateList>    // Listas privadas del usuario
}

const initialState : IUserState = {
    user: null,
    userSearch: null,
    privateLists: [],
}


export {initialState};
export type {IUserState};
