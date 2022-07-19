import {AnyAction, combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {AppDispatchType, AppErrorType} from './actionTypes';
import {IAppState, initialState} from "./IAppState";


const loadingReducer = (state: boolean = initialState.loading,
                        action: AppDispatchType) : boolean => {
    switch (action.type) {
        case actionTypes.LOADING:
            return true;
        case actionTypes.LOADED:
            return false;
        case actionTypes.ERROR:
            return false;
        default:
            return state;
    }
};


const errorReducer = (state: AppErrorType = initialState.error,
                      action: AnyAction) : AppErrorType => {
    switch (action.type) {
        case actionTypes.ERROR:
            return action.error;
        default:
            return state;
    }
}


const appReducer = combineReducers<IAppState>({
    loading: loadingReducer,
    error: errorReducer
});

export default appReducer;
