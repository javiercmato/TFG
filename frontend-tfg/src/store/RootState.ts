/** Tipo del estado raíz de la aplicación */
import {appInitialState, IAppState} from "../App";
import {IUserState, usersInitialState} from "../Users";
import {IIngredientState, ingredientsInitialState} from "../Ingredients";


export interface RootState {
    app: IAppState,
    users: IUserState,
    ingredients: IIngredientState,
}


export const initialState: RootState = {
    app: appInitialState,
    users: usersInitialState,
    ingredients: ingredientsInitialState
}
