import {User} from "../../Domain";


/** Tipo de estado para información asociada a los usuarios */
interface IUserState {
    user: Nullable<User>            // Datos del usuario autenticado
}

const initialState : IUserState = {
    user: null,
}


export {initialState};
export type {IUserState};
