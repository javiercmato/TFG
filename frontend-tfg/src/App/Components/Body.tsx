import {Route, Routes} from 'react-router-dom';
import {Container} from "react-bootstrap";
import AppGlobalComponents from "./AppGlobalComponents";
import Home from "./Home";
import {ChangePassword, Login, Logout, SignUp, userRedux} from "../../Users";
import {body} from './styles/body';
import {useAppSelector} from "../../store";


const Body = () => {
    const isUserLogged = useAppSelector(userRedux.selectors.isLoggedIn);

    return (
        <Container className={Body.name} style={body}>
            <br/>
            <AppGlobalComponents />
            <Routes>
                <Route path="/" element={<Home />} />
                {/* ****************************** USERS ****************************** */}
                <Route path="/signUp" element={<SignUp />} />
                <Route path="/login" element={<Login />} />
                {isUserLogged && <Route path="/logout" element={<Logout />} />}
                {isUserLogged && <Route path="/changePassword" element={<ChangePassword />} />}
                <Route element={<Home />} />
            </Routes>
        </Container>
    )
}


export default Body;
