import rootReducer from "./rootReducer";
import {configureStore} from "@reduxjs/toolkit";


const store = configureStore({
    reducer: rootReducer
});


export type RootStateType = ReturnType<typeof rootReducer>;
export default store;
