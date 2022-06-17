import * as actions from './actions';
import appReducer from "./reducer";


export default {actions, reducer: appReducer};
export {initialState} from './IAppState';
export type {IAppState} from './IAppState';
