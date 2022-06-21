import {appRedux} from '../Application';
import {useAppDispatch, useAppSelector} from "../../store";
import ErrorDialog from "./ErrorDialog";
import {Spinner} from "react-bootstrap";


const ConnectedErrorDialog = () => {
    const errorData = useAppSelector(appRedux.selectors.getAppError);
    const dispatch = useAppDispatch();
    let onClose : NoArgsCallbackFunction = () => dispatch(appRedux.actions.error(null));

    if (errorData === null) return null;
    return <ErrorDialog error={errorData} onCloseCallback={onClose} />
}

const ConnectedLoader = () => {
    const isLoading = useAppSelector(appRedux.selectors.isLoading);

    return (isLoading) ? <Spinner animation={"border"} /> : null;
}


const AppGlobalComponents = () => (
    <div className={AppGlobalComponents.name}>
        <ConnectedErrorDialog />
        <ConnectedLoader />
    </div>
)


export default AppGlobalComponents;
