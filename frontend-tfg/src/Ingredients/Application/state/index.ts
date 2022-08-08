import * as actions from './actions';
import ingredientsReducer from "./reducer";
import * as selectors from './selectors';


export default {actions, reducer: ingredientsReducer, selectors};
export {initialState} from './IIngredientState';
export type {IIngredientState} from './IIngredientState';
