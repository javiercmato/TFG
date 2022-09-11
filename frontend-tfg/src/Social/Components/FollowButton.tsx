import {useAppDispatch, useAppSelector} from "../../store";
import {Button} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {ErrorDto} from "../../App";
import {socialRedux} from "../Application";
import {userRedux} from "../../Users";

interface Props {
    targetUserID: string,
    onFollowCallback: () => void,
    onErrorCallback: (error: ErrorDto) => void,
}

const FollowButton = ({targetUserID, onFollowCallback, onErrorCallback}: Props) => {
    const dispatch = useAppDispatch();
    const requestorID = useAppSelector(userRedux.selectors.selectUserID);

    const handleClick = () => {
        let onSuccess = () => {
            onFollowCallback()
        }
        let onError = (error: ErrorDto) => {
            onErrorCallback(error);
        }
        dispatch(socialRedux.actions.followUserAsyncAction(requestorID, targetUserID, onSuccess, onError));
    }

    return (
        <Button
            variant="success"
            onClick={handleClick}
        >
            <FormattedMessage id="social.components.FollowButton.text" />
        </Button>
    )
}

export default FollowButton;
export type {Props as FollowButtonProps};
