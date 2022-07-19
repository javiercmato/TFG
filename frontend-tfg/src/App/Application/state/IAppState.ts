import {AppErrorType} from "./actionTypes";

/** Tipo de estado de la aplicación */
interface IAppState {
    loading: boolean,
    error: AppErrorType
}

const initialState : IAppState = {
    loading : false,
    error: null
}

export {initialState};
export type {IAppState};
