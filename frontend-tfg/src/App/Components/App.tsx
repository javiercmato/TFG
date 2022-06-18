import React from 'react';
import './styles/App.css';
import {BrowserRouter as Router} from "react-router-dom";
import Body from "./Body";

const App = () => {

    return (
        <div>
            <Router>
                <div>
                    <Body />
                </div>
            </Router>
        </div>
  );
}

export default App;
