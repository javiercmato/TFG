import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {UserDispatchType} from './actionTypes';
import {initialState, IUserState} from "./IUserState";
import {User} from "../../Domain";


const user = (state: Nullable<User> = initialState.user,
                     action: UserDispatchType) : Nullable<User> => {
    switch (action.type) {
        case actionTypes.SIGN_UP:
            return action.payload.user;

        default:
            return state;
    }
}


const userReducer = combineReducers<IUserState>({
    user: user,
});

export default userReducer;
