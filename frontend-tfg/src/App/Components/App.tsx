import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter as Router} from "react-router-dom";
import Body from "./Body";
import Header from "./Header";
import Footer from "./Footer";

const App = () => {
    document.title = "TFG";

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
