import {ErrorDto} from "../../Domain";


export const LOADING : string = 'App/Loading';
export const LOADED : string = 'App/Loaded';
export const ERROR : string = 'App/Error';

/* ******************** Tipos de datos ******************** */

export type AppErrorType = Error | ErrorDto | null;

/* ******************** Tipos de las acciones ******************** */
export interface LoadingActionType {
    type: string
}

export interface LoadedActionType {
    type: string
}

export interface ErrorActionType {
    type: string,
    error: AppErrorType
}


export type AppDispatchType = LoadingActionType | LoadedActionType | ErrorActionType;
