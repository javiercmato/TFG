import * as actions from './actions';
import ingredientsReducer from "./reducer";


export default {actions, reducer: ingredientsReducer};
export {initialState} from './IIngredientState';
export type {IIngredientState} from './IIngredientState';
