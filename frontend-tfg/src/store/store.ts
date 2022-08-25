import rootReducer, {RootStateType} from "./rootReducer";
import {configureStore} from "@reduxjs/toolkit";
import {throttle} from 'lodash';
import {User, usersInitialState} from "../Users";
import {initialState} from "./RootState";

const NAVIGATOR_USER_STATE_KEY: string = String(process.env.REACT_APP_NAVIGATOR_USER_STATE_KEY);
const STATE_REFRESH_RATE: number = Number(process.env.REACT_APP_NAVIGATOR_STATE_REFRESH_RATE);


/** Carga el estado de Redux guardado en el navegador */
export const loadUserState = (): Nullable<User> => {
    // Obtiene el estado del almacenamiento del navegador
    const loadedState = localStorage.getItem(NAVIGATOR_USER_STATE_KEY);
    if (loadedState === null) return null;

    // Parsea estado a JSON y lo devuelve
    return JSON.parse(loadedState);
}

/** Guarda el estado de Redux recibido en el navegador */
export const saveUserState = (state: Nullable<User>) => {
    const stringifiedState = JSON.stringify(state);
    localStorage.setItem(`${NAVIGATOR_USER_STATE_KEY}`, stringifiedState);
}


const storedUserState = loadUserState();

const store = configureStore<RootStateType>({
    reducer: rootReducer,
    preloadedState: {
        ...initialState,
        users: {
            ...usersInitialState,
            user: storedUserState,
        }
    },
});

// Cada vez que haya un cambio (dispatch de alguna action), se guarda estado en navegador
store.subscribe(
    throttle(() => {
        // Guarda solo la información del usuario loggeado (el resto la deja con su estado inicial)
        let userState = store.getState().users.user;
        saveUserState(userState);
    },
    STATE_REFRESH_RATE * 1000)
);


export type AppDispatch = typeof store.dispatch;
export default store;
