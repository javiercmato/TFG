import {AuthenticatedUser, User} from "../../Domain";
import PrivateList from "../../Domain/PrivateList";
import PrivateListSummaryDTO from "../../Infrastructure/PrivateListSummaryDTO";


/* ******************** Nombres de las acciones ******************** */

export const SIGN_UP : string = 'users/signUp';
export const LOGIN : string = 'users/login';
export const LOGOUT : string = 'users/logout';
export const CHANGE_PASSWORD : string = 'users/changePassword';
export const FIND_USER_BY_ID : string = 'users/findUserByID';
export const UPDATE_PROFILE : string = 'users/updateProfile';
export const BAN_USER : string = 'users/banUser';
export const DELETE_USER : string = 'users/deleteUser';
export const CREATE_PRIVATE_LIST : string = 'users/createPrivateList';
export const GET_PRIVATE_LISTS : string = 'users/getPrivateLists';
export const GET_PRIVATE_LIST_DETAILS: string = 'users/getPrivateListDetails';
export const ADD_RECIPE_TO_PRIVATE_LIST: string = 'users/addRecipeToPrivateList';
export const REMOVE_RECIPE_FROM_PRIVATE_LIST: string = 'users/removeRecipeFromPrivateList';
export const DELETE_PRIVATE_LIST: string = 'users/deletePrivateList';
export const CLEAR_USER_DETAILS: string = 'users/clearUserDetails';

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

export interface CreatePrivateListActionType {
    type: string,
    payload: PrivateList,
}

export interface GetPrivateListsActionType {
    type: string,
    payload: Array<PrivateListSummaryDTO>,
}

export interface GetPrivateListDetailsActionType {
    type: string,
    payload: PrivateList,
}

export interface AddRecipeToPrivateListActionType {
    type: string,
}

export interface RemoveRecipeFromPrivateListActionType {
    type: string,
}

export interface DeletePrivateListActionType {
    type: string,
}

export interface ClearUserDetailsActionType {
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
    | CreatePrivateListActionType
    | GetPrivateListsActionType
    | GetPrivateListDetailsActionType
    | AddRecipeToPrivateListActionType
    | RemoveRecipeFromPrivateListActionType
    | DeletePrivateListActionType
    | ClearUserDetailsActionType
;
