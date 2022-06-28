import React, {useEffect} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';
import {BrowserRouter as Router} from "react-router-dom";
import Body from "./Body";
import Header from "./Header";
import Footer from "./Footer";
import {useAppDispatch} from "../../store";
import {userRedux} from "../../Users";

const App = () => {
    document.title = "TFG";
    const dispatch = useAppDispatch();

    // Al cargar la apliación, intentar iniciar sesión automáticamente con el token
    useEffect(() => {
        let onReauthenticate: NoArgsCallbackFunction = () => dispatch(userRedux.actions.logoutAsyncAction());
        dispatch(userRedux.actions.loginWithServiceTokenAsyncAction(onReauthenticate));
    });

    return (
        <div className={App.name}>
            <Router>
                <Header />
                <Body />
                <Footer />
            </Router>
        </div>
  );
}

export default App;
