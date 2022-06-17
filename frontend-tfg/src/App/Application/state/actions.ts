import * as actionTypes from './actionTypes';
import {AppDispatchType, AppErrorType} from './actionTypes';

/* ************************* DISPATCHABLE ACTIONS ******************** */
export const loading = () : AppDispatchType => ({
    type: actionTypes.LOADING
});

export const loaded = () : AppDispatchType => ({
    type: actionTypes.LOADING
});

export const error = (appError : AppErrorType) : AppDispatchType => ({
    type: actionTypes.ERROR,
    error: appError
});
