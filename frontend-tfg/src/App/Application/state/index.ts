import * as actions from './actions';
import appReducer from "./reducer";
import * as selectors from './selectors';


export default {actions, reducer: appReducer, selectors};
export {initialState} from './IAppState';
export type {IAppState} from './IAppState';
