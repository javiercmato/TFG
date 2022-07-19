import {ThunkAction} from 'redux-thunk';
import {AnyAction} from '@reduxjs/toolkit';
import {RootState} from "./RootState";

/* INFO: https://redux.js.org/usage/usage-with-typescript#type-checking-redux-thunks */
export type AppThunk<ReturnType = void> = ThunkAction<ReturnType, RootState, unknown, AnyAction>
