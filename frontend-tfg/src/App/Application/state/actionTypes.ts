import {ErrorDto} from "../../Domain";

/* ******************** Nombres de las acciones ******************** */

export const LOADING : string = 'app/Loading';
export const LOADED : string = 'app/Loaded';
export const ERROR : string = 'app/Error';

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
