/** Tipo del estado raíz de la aplicación */
import {appInitialState, IAppState} from "../App";
import {IUserState, usersInitialState} from "../Users";
import {IIngredientState, ingredientsInitialState} from "../Ingredients";
import {IRecipeState, recipesInitialState} from "../Recipes";
import {ISocialState, socialInitialState} from "../Social";


export interface RootState {
    app: IAppState,
    users: IUserState,
    ingredients: IIngredientState,
    recipes: IRecipeState,
    social: ISocialState
}


export const initialState: RootState = {
    app: appInitialState,
    users: usersInitialState,
    ingredients: ingredientsInitialState,
    recipes: recipesInitialState,
    social: socialInitialState
}
