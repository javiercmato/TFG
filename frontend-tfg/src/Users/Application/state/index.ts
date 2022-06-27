import * as actions from './actions';
import userReducer from "./reducer";
import * as selectors from './selectors';


export default {actions, reducer: userReducer, selectors};
export {initialState} from './IUserState';
export type {IUserState} from './IUserState';
