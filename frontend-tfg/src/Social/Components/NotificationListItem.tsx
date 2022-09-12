import {Notification} from "../Domain";
import {Col, Container, Row} from "react-bootstrap";


interface Props {
    notification: Notification
}

const NotificationListItem = ({notification}: Props) => {

    return (
        <Container>
            <Row>
                <Col md={10}>
                    <b>{notification.title}</b>
                </Col>

                <Col>
                    {notification.createdAt}
                </Col>
            </Row>

            <Row>
                {notification.message}
            </Row>
        </Container>
    )
}

export default NotificationListItem;
