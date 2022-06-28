import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {SignUpUserActionType, UserDispatchType} from './actionTypes';
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

        default:
            return state;
    }
}


const userReducer = combineReducers<IUserState>({
    user: user,
});

export default userReducer;
