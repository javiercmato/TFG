import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';
import {GetFollowersActionType, GetFollowingsActionType, SocialDispatchType} from './actionTypes';
import {initialState, ISocialState} from "./ISocialState";
import {Follow} from "../../Domain";
import {Search} from "../../../App";


const followings = (state: Search<Follow> = initialState.followings,
                    action: SocialDispatchType) : Search<Follow> => {
    switch (action.type) {
        case actionTypes.GET_FOLLOWINGS: {
            let search: Search<Follow> = (action as GetFollowingsActionType).payload;

            return (search);
        }

        default:
            return state;
    }
}


const followers = (state: Search<Follow> = initialState.followers,
                    action: SocialDispatchType) : Search<Follow> => {
    switch (action.type) {
        case actionTypes.GET_FOLLOWERS: {
            let search: Search<Follow> = (action as GetFollowersActionType).payload;

            return (search);
        }

        default:
            return state;
    }
}


const socialReducer = combineReducers<ISocialState>({
    followings: followings,
    followers: followers,
});

export default socialReducer;
