/* ******************** Nombres de las acciones ******************** */

import {Follow} from "../../Domain";

export const FOLLOW_USER: string = 'social/followUser';
export const UNFOLLOW_USER: string = 'social/unfollowUser';
export const CHECK_USER_FOLLOWS_TARGET: string = 'social/checkUserFollowsTarget';

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

export type SocialDispatchType = FollowUserActionType
    | UnfollowUserActionType
    | CheckUserFollowsTargetActionType
;
