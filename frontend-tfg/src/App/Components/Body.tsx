import {Route, Routes} from 'react-router-dom';
import {Container} from "react-bootstrap";
import AppGlobalComponents from "./AppGlobalComponents";
import Home from "./Home";
import {SignUp} from "../../Users";
import {body} from './styles/body';


const Body = () => {

    return (
        <Container className={Body.name} style={body}>
            <br/>
            <AppGlobalComponents />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/signUp" element={<SignUp />} />
                <Route element={<Home />}></Route>
            </Routes>
        </Container>
    )
}


export default Body;
