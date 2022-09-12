import {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../store";
import {socialRedux} from "../Application";
import {SearchCriteria} from "../../App";
import searchCriteria from "../../App/Domain/common/SearchCriteria";
import {userRedux} from "../../Users";
import {Alert, ListGroup, ListGroupItem, Offcanvas} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import NotificationListItem from "./NotificationListItem";

interface Props {
    shouldShow: boolean,
    onHideCallback: any,
}

const NotificationsList = ({shouldShow, onHideCallback} : Props) => {
    const dispatch = useAppDispatch();
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const [page, setPage] = useState<number>(0);
    const [show, setShow] = useState<boolean>(shouldShow);
    const notifications = useAppSelector(socialRedux.selectors.getNotifications).result;

    // Pide las notificaciones cada vez que se abra el componente
    useEffect( () => {
        let notificationsCriteria: SearchCriteria = {
            ...searchCriteria,
            userID: userID!,
            page: page,
        }

        let onSuccess = () => {}
        let onError = () => {}
        dispatch(socialRedux.actions.getUnreadNotificationsAsyncAction(notificationsCriteria, onSuccess, onError));
    }, [userID, page])


    return (
        <Offcanvas show={show} onHide={() => onHideCallback()}>
            <Offcanvas.Header closeButton>
                <Offcanvas.Title>
                    <FormattedMessage id="common.fields.notifications" />
                </Offcanvas.Title>
            </Offcanvas.Header>

            <Offcanvas.Body>
                {(notifications === null) ?
                    <Alert variant="info">
                        <FormattedMessage id="common.alerts.noResults" />
                    </Alert>
                    :
                <ListGroup>
                    {notifications.items.map((notification) =>
                        <ListGroupItem>
                            <NotificationListItem notification={notification} />
                        </ListGroupItem>
                    )}
                </ListGroup>
                }
            </Offcanvas.Body>
        </Offcanvas>
    )
}

export default NotificationsList;
export type {Props as NotificationsListProps};
