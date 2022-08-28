import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../Application";
import {Col, Container, Row} from "react-bootstrap";
import {FormattedMessage, useIntl} from "react-intl";
import {FormEvent} from "react";
import CreatePrivateList from "./CreatePrivateList";

const PrivateListsPage = () => {
    const intl = useIntl();
    const dispatch = useAppDispatch();
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const privateLists = useAppSelector(userRedux.selectors.selectPrivateLists);

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
    }


    return (
        <Container>
            <Row>
                <Col md={4}>
                    <FormattedMessage id='users.components.PrivateListsPage.title' />
                    <CreatePrivateList />
                </Col>

                <Col></Col>
            </Row>
        </Container>
    )
}

export default PrivateListsPage;
