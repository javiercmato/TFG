import * as actions from './actions';
import socialReducer from './reducer';


export type {ISocialState} from './ISocialState';
export {initialState} from './ISocialState';
export default {actions, reducer: socialReducer};
