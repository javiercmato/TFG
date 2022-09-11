/* ******************** Nombres de las acciones ******************** */

import {Follow} from "../../Domain";
import {Search} from "../../../App";

export const FOLLOW_USER: string = 'social/followUser';
export const UNFOLLOW_USER: string = 'social/unfollowUser';
export const CHECK_USER_FOLLOWS_TARGET: string = 'social/checkUserFollowsTarget';
export const GET_FOLLOWERS: string = 'social/getFollowers';
export const GET_FOLLOWINGS: string = 'social/getFollowings';

/* ******************** Tipos de las acciones ******************** */

export interface FollowUserActionType {
    type: string,
    payload: Follow
}

export interface UnfollowUserActionType {
    type: string,
}

export interface CheckUserFollowsTargetActionType {
    type: string,
    payload: boolean,
}

export interface GetFollowersActionType {
    type: string,
    payload: Search<Follow>
}

export interface GetFollowingsActionType {
    type: string,
    payload: Search<Follow>
}

export type SocialDispatchType = FollowUserActionType
    | UnfollowUserActionType
    | CheckUserFollowsTargetActionType
    | GetFollowersActionType
    | GetFollowingsActionType
;
