import {RootState} from "../../../store";
import {ISocialState} from "./ISocialState";
import {Search} from "../../../App";
import {Follow} from "../../Domain";

const getModuleState = (state: RootState): ISocialState => state.social;


/* ******************** DATOS DE SEGUIDORES ******************** */

export const getFollowers = (state: RootState) : Search<Follow> => getModuleState(state).followers;


export const getFollowings = (state: RootState) : Search<Follow> => getModuleState(state).followings;


/* ******************** DATOS DE SIGUIENDOS ******************** */
