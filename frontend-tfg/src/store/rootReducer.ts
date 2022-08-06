import {combineReducers} from "@reduxjs/toolkit";
import {RootState} from "./RootState";

import {appRedux} from "../App";
import {userRedux} from '../Users';
import {ingredientsRedux} from '../Ingredients';

const rootReducer = combineReducers<RootState>({
    app: appRedux.reducer,
    users: userRedux.reducer,
    ingredients: ingredientsRedux.reducer,
})


export default rootReducer;
export type RootStateType = ReturnType<typeof rootReducer>;
