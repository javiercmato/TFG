/** Tipo del estado raíz de la aplicación */
import {appInitialState, IAppState} from "../App";


export interface RootState {
    app: IAppState
}


export const initialState: RootState = {
    app: appInitialState
}
