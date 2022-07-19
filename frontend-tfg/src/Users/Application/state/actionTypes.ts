import {AuthenticatedUser, User} from "../../Domain";


/* ******************** Nombres de las acciones ******************** */

export const SIGN_UP : string = 'users/signUp';
export const LOGIN : string = 'users/login';
export const LOGOUT : string = 'users/logout';
export const CHANGE_PASSWORD : string = 'users/changePassword';
export const FIND_USER_BY_NICKNAME : string = 'users/findUserByNickname';
export const UPDATE_PROFILE : string = 'users/updateProfile';
export const BAN_USER : string = 'users/banUser';
export const DELETE_USER : string = 'users/deleteUser';

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

export interface FindUserActionType {
    type: string,
    payload: User
}

export interface UpdateProfileActionType {
    type: string,
    payload: User
}

export interface BanUserActionType {
    type: string,
    payload: boolean
}

export interface DeleteUserActionType {
    type: string,
}


export type UserDispatchType = SignUpUserActionType
    | LoginActionType
    | LogoutActionType
    | ChangePasswordActionType
    | FindUserActionType
    | UpdateProfileActionType
    | BanUserActionType
    | DeleteUserActionType
;
