import * as actions from './actions';
import socialReducer from './reducer';
import * as selectors from './selectors';


export type {ISocialState} from './ISocialState';
export {initialState} from './ISocialState';
export default {actions, reducer: socialReducer, selectors};
