import {appRedux} from "../App";
import {combineReducers} from "@reduxjs/toolkit";
import {RootState} from "./RootState";


const rootReducer = combineReducers<RootState>({
    app: appRedux.reducer
})


export default rootReducer;
