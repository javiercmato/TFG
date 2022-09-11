import {Button} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {ErrorDto} from "../../App";
import {socialRedux} from "../Application";
import {userRedux} from "../../Users";
import {useAppDispatch, useAppSelector} from "../../store";

interface Props {
    targetUserID: string,
    onUnfollowCallback: () => void,
    onErrorCallback: (error: ErrorDto) => void,
}

const UnfollowButton = ({targetUserID, onUnfollowCallback, onErrorCallback}: Props) => {
    const dispatch = useAppDispatch();
    const requestorID = useAppSelector(userRedux.selectors.selectUserID);

    const handleClick = () => {
        let onSuccess = () => {
            onUnfollowCallback()
        }
        let onError = (error: ErrorDto) => {
            onErrorCallback(error);
        }
        dispatch(socialRedux.actions.unfollowUserAsyncAction(requestorID, targetUserID, onSuccess, onError));
    }

    return (
        <Button
            variant="danger"
            onClick={handleClick}
        >
            <FormattedMessage id="social.components.UnfollowButton.text" />
        </Button>
    )
}

export default UnfollowButton;
export type {Props as UnfollowButtonProps};
