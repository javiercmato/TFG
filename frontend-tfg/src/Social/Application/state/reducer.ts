import {combineReducers} from "redux";
import {SocialDispatchType} from './actionTypes';
import {initialState, ISocialState} from "./ISocialState";
import {Follow} from "../../Domain";


const followings = (state: Array<Follow> = initialState.followings,
                    action: SocialDispatchType) : Array<Follow> => {
    switch (action.type) {


        default:
            return state;
    }
}


const followers = (state: Array<Follow> = initialState.followers,
                    action: SocialDispatchType) : Array<Follow> => {
    switch (action.type) {


        default:
            return state;
    }
}


const socialReducer = combineReducers<ISocialState>({
    followings: followings,
    followers: followers,
});

export default socialReducer;
