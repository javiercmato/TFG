import {User} from "../../Domain";
import PrivateList from "../../Domain/PrivateList";
import PrivateListSummaryDTO from "../../Infrastructure/PrivateListSummaryDTO";


/** Tipo de estado para información asociada a los usuarios */
interface IUserState {
    user: Nullable<User>,                           // Datos del usuario autenticado
    userSearch: Nullable<User>,                     // Datos de la búsqueda de un usuario
    privateLists: Array<PrivateListSummaryDTO>      // Listas privadas del usuario
    privateListDetails: Nullable<PrivateList>       // Información de una lista privada
}

const initialState : IUserState = {
    user: null,
    userSearch: null,
    privateLists: [],
    privateListDetails: null,
}


export {initialState};
export type {IUserState};
