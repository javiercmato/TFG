import {RootState} from "../../../store";
import {IUserState} from "./IUserState";
import {User} from "../../Domain";

const getModuleState = (state: RootState) : IUserState => state.users;

const getUserModule = (state: RootState) : Nullable<User> => getModuleState(state).user;

/* ******************** DATOS DE USUARIO ******************** */
export const isLoggedIn = (state: RootState) : boolean => getUserModule(state) != null;

export const selectNickname = (state: RootState) : string => getUserModule(state)?.nickname!;

