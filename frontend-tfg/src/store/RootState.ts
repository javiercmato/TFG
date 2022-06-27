/** Tipo del estado raíz de la aplicación */
import {appInitialState, IAppState} from "../App";
import {IUserState, usersInitialState} from "../Users";


export interface RootState {
    app: IAppState,
    users: IUserState
}


export const initialState: RootState = {
    app: appInitialState,
    users: usersInitialState,
}
