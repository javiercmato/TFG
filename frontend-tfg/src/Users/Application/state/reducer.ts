import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {
    BanUserActionType,
    FindUserActionType,
    SignUpUserActionType,
    UpdateProfileActionType,
    UserDispatchType
} from './actionTypes';
import {initialState, IUserState} from "./IUserState";
import {User} from "../../Domain";


const user = (state: Nullable<User> = initialState.user,
              action: UserDispatchType) : Nullable<User> => {
    switch (action.type) {
        case actionTypes.SIGN_UP:
            return (action as SignUpUserActionType).payload.user;

        case actionTypes.LOGIN:
            return (action as SignUpUserActionType).payload.user;

        case actionTypes.LOGOUT:
            return initialState.user;

        case actionTypes.CHANGE_PASSWORD:
            return state;

        case actionTypes.UPDATE_PROFILE:
            return (action as UpdateProfileActionType).payload;

        case actionTypes.DELETE_USER:
            return initialState.user;

        default:
            return state;
    }
}

const userSearch = (state: Nullable<User> = initialState.userSearch,
                    action: UserDispatchType) : Nullable<User> => {
    switch (action.type) {
        case actionTypes.FIND_USER_BY_NICKNAME:
            return (action as FindUserActionType).payload;

        case actionTypes.BAN_USER:
            let payload: boolean = (action as BanUserActionType).payload;
            return {
                ...state,
                isBannedByAdmin: payload
            }

        default:
            return state;
    }
}


const userReducer = combineReducers<IUserState>({
    user: user,
    userSearch: userSearch
});

export default userReducer;
