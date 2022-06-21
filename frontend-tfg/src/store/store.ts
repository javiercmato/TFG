import rootReducer, {RootStateType} from "./rootReducer";
import {configureStore} from "@reduxjs/toolkit";


const store = configureStore<RootStateType>({
    reducer: rootReducer,
});


export type AppDispatch = typeof store.dispatch;
export default store;
