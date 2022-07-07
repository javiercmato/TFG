import {AuthenticatedUser, User} from "../../Domain";


/* ******************** Nombres de las acciones ******************** */

export const SIGN_UP : string = 'users/signUp';
export const LOGIN : string = 'users/login';
export const LOGOUT : string = 'users/logout';
export const CHANGE_PASSWORD : string = 'users/changePassword';
export const GET_USER_PROFILE : string = 'users/getUserProfile';

/* ******************** Tipos de datos ******************** */


/* ******************** Tipos de las acciones ******************** */

export interface SignUpUserActionType {
    type: string,
    payload: AuthenticatedUser
}

export interface LoginActionType {
    type: string,
    payload: AuthenticatedUser
}

export interface LogoutActionType {
    type: string,
}
export interface ChangePasswordActionType {
    type: string,
}

export interface GetUserProfileActionType {
    type: string,
    payload: User
}


export type UserDispatchType = SignUpUserActionType
    | LoginActionType
    | LogoutActionType
    | ChangePasswordActionType
    | GetUserProfileActionType;
