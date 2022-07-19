import {useAppDispatch} from "../../store";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {userRedux} from "../Application";


const Logout = () => {
    const dispatch = useAppDispatch();
    const navigator = useNavigate();

    useEffect(() => {
        dispatch(userRedux.actions.logoutAsyncAction());
        navigator("/");         // Vuelve a la página de inicio
    });

    return null;
}


export default Logout;
