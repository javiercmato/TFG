import {Route, Routes} from 'react-router-dom';
import {Container} from "react-bootstrap";
import AppGlobalComponents from "./AppGlobalComponents";
import Home from "./Home";


const Body = () => {

    return (
        <Container className={Body.name}>
            <br/>
            <AppGlobalComponents />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route element={<Home />}></Route>
            </Routes>
        </Container>
    )
}


export default Body;
