import {User} from "../Users";
import {ReactNode} from "react";
import rootReducer from "../store/rootReducer";
import {createMemoryHistory} from 'history';
import {render} from "@testing-library/react";
import { Provider } from "react-redux";
import { Router, useLocation } from "react-router-dom";
import {configureStore} from "@reduxjs/toolkit";

/** Genera datos de un usuario en función de su ID (si es administrador o no) */
export const generateValidUser = (userID: string, nickname: string) : User => {
    const password: string = "password";
    const name: string = "name";
    const surname: string = "surname";
    const email: string = `${nickname}@email.es`;
    const avatar: string = '';
    const role: string = 'USER';
    const isBannedByAdmin: boolean = false;
    const registerdate: Date = new Date();

    return {
        userID,
        nickname,
        password,
        name,
        surname,
        email,
        avatar,
        role,
        isBannedByAdmin,
        registerdate
    };
}


/* ****************************** TESTS DE COMPONENTE ****************************** */
/** Renderiza el componente recibido con un estado inicial en el store de Redux */
export const renderComponent = (component: ReactNode,
                                initialState: any = {}) => {
    const store = configureStore({
        reducer: rootReducer,
        preloadedState: initialState
    });
    store.dispatch = jest.fn();
    const history = createMemoryHistory();
    const location = useLocation();

    return {history, ...render(
        <Provider store={store}>
            <Router navigator={history} location={location}>
                {component}
            </Router>
        </Provider>
    )};
}
