import React, {MouseEventHandler} from "react";
import {Button} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {useSelector} from "react-redux";
import {userRedux} from "../Application";
import {useAppDispatch} from "../../store";

interface Props {
    onErrorCallback: CallbackFunction,
}

const BanUserButton = ({onErrorCallback}: Props) => {
    const dispatch = useAppDispatch();
    const adminID = useSelector(userRedux.selectors.selectUserID);
    const targetUser = useSelector(userRedux.selectors.selectUserSearch);
    const isCurrentUserAdmin = useSelector(userRedux.selectors.selectIsAdmin);
    const isTargetUserAdmin = useSelector(userRedux.selectors.isUserSearchAdmin);
    let isBanned : boolean = targetUser?.isBannedByAdmin!;


    const handleClick: MouseEventHandler<HTMLButtonElement> = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        let onSuccess: CallbackFunction = () => {};
        let onErrors: CallbackFunction = (err) => {onErrorCallback(err)};

        dispatch(userRedux.actions.banUserAsyncAction(adminID, targetUser?.userID!, onSuccess, onErrors));
    }

    // Solo un usuario administrador puede banear usuarios
    if (!isCurrentUserAdmin) return null;

    // El administrador no puede banearse a él mismo
    if (isTargetUserAdmin) return null;


    return (
        <Button
            variant="secondary"
            onClick={handleClick}
        >
            {(isBanned) ?
                <FormattedMessage id="users.components.BanUserButton.unbanButton" />
                :
                <FormattedMessage id="users.components.BanUserButton.banButton"/>
            }
        </Button>
    )
}

export default BanUserButton;
