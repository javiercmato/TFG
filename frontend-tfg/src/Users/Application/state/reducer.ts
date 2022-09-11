import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {
    BanUserActionType,
    CreatePrivateListActionType,
    FindUserActionType,
    GetPrivateListDetailsActionType,
    GetPrivateListsActionType,
    SignUpUserActionType,
    UpdateProfileActionType,
    UserDispatchType
} from './actionTypes';
import * as socialActionTypes from '../../../Social/Application/state/actionTypes';
import {CheckUserFollowsTargetActionType} from '../../../Social/Application/state/actionTypes';
import {initialState, IUserState} from "./IUserState";
import {User} from "../../Domain";
import PrivateList from "../../Domain/PrivateList";
import PrivateListSummaryDTO from "../../Infrastructure/PrivateListSummaryDTO";


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

        case actionTypes.BAN_USER: {

            let payload: boolean = (action as BanUserActionType).payload;
            return {
                ...state,
                isBannedByAdmin: payload
            }
        }

        case socialActionTypes.CHECK_USER_FOLLOWS_TARGET: {
            let payload: boolean = (action as CheckUserFollowsTargetActionType).payload;

            return ({
                ...state,
                isFollowedByUser: payload
            })
        }

        default:
            return state;
    }
}

const privateList = (state: Array<PrivateListSummaryDTO> = initialState.privateLists,
                      action: UserDispatchType) : Array<PrivateListSummaryDTO> => {
    switch (action.type) {
        case actionTypes.CREATE_PRIVATE_LIST: {
            let payload: PrivateListSummaryDTO = (action as CreatePrivateListActionType).payload;

            return [...state, payload];
        }

        case actionTypes.GET_PRIVATE_LISTS: {
            let payload: Array<PrivateListSummaryDTO> = (action as GetPrivateListsActionType).payload;

            return payload;
        }

        case actionTypes.ADD_RECIPE_TO_PRIVATE_LIST: {
            return state;
        }

        case actionTypes.REMOVE_RECIPE_FROM_PRIVATE_LIST: {
            return state;
        }

        case actionTypes.DELETE_PRIVATE_LIST: {
            return state;
        }

        default:
            return state;
    }
}

const privateListDetails = (state: Nullable<PrivateList> = initialState.privateListDetails,
                     action: UserDispatchType) : Nullable<PrivateList> => {
    switch (action.type) {
        case actionTypes.GET_PRIVATE_LIST_DETAILS: {
            let payload: PrivateList = (action as GetPrivateListDetailsActionType).payload;

            return payload;
        }

        default:
            return state;
    }
}


const userReducer = combineReducers<IUserState>({
    user: user,
    userSearch: userSearch,
    privateLists: privateList,
    privateListDetails: privateListDetails,
});

export default userReducer;
