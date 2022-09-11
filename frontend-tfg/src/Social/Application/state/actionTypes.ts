/* ******************** Nombres de las acciones ******************** */

import {Follow} from "../../Domain";

export const FOLLOW_USER: string = 'social/followUser';
export const UNFOLLOW_USER: string = 'social/unfollowUser';

/* ******************** Tipos de las acciones ******************** */

export interface FollowUserActionType {
    type: string,
    payload: Follow
}

export interface UnfollowUserActionType {
    type: string,
}


export type SocialDispatchType = FollowUserActionType
    | UnfollowUserActionType
;
