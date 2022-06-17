import {RootState} from "../../../store";
import {IUserState} from "./IUserState";
import {User} from "../../Domain";

const getModuleState = (state: RootState) : IUserState => state.users;

const getUserModule = (state: RootState) : Nullable<User> => getModuleState(state).user;


