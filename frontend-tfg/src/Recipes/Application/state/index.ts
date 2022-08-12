import * as actions from './actions';
import recipesReducer from './reducer';


export type {IRecipeState} from './IRecipeState'
export {initialState} from './IRecipeState';
export default {actions, reducer: recipesReducer};
