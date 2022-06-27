import {RootState} from '../../../store';
import {IAppState} from './IAppState';
import {AppErrorType} from "./actionTypes";

const getModuleState = (state: RootState) : IAppState => state.app;

export const isLoading = (state: RootState) : boolean => getModuleState(state).loading;

export const getAppError = (state: RootState) : AppErrorType => getModuleState(state).error;
