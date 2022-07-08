import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {FindUserActionType, SignUpUserActionType, UpdateProfileActionType, UserDispatchType} from './actionTypes';
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

        default:
            return state;
    }
}

const userSearch = (state: Nullable<User> = initialState.userSearch,
                    action: UserDispatchType) : Nullable<User> => {
    switch (action.type) {
        case actionTypes.FIND_USER_BY_NICKNAME:
            return (action as FindUserActionType).payload;

        default:
            return state;
    }
}


const userReducer = combineReducers<IUserState>({
    user: user,
    userSearch: userSearch
});

export default userReducer;
