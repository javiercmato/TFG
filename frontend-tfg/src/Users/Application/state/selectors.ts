import {RootState} from "../../../store";
import {IUserState} from "./IUserState";
import {User} from "../../Domain";

const getModuleState = (state: RootState) : IUserState => state.users;

const getUserModule = (state: RootState) : Nullable<User> => getModuleState(state).user;

const getUserSearchModule = (state: RootState) : Nullable<User> => getModuleState(state).userSearch;

/* ******************** DATOS DE USUARIO ******************** */

export const isLoggedIn = (state: RootState) : boolean => getUserModule(state) != null;

export const selectNickname = (state: RootState) : string => getUserModule(state)?.nickname!;

export const selectUserID = (state: RootState) : string => getUserModule(state)?.userID!;


/* ******************** DATOS DE BÚSQUEDA DE USUARIO ******************** */

export const selectUserSearch = (state: RootState) : User => getUserSearchModule(state)!;

