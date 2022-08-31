import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../Application";
import {Alert, Col, Container, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import React, {useEffect, useState} from "react";
import CreatePrivateList from "./CreatePrivateList";
import PrivateListSummary, {PrivateListSummaryProps} from "./PrivateListSummary";
import PrivateListDetails, {PrivateListDetailsProps} from "./PrivateListDetails";

const PrivateListsPage = () => {
    const dispatch = useAppDispatch();
    const privateLists = useAppSelector(userRedux.selectors.selectPrivateLists);
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    let hasPrivateLists: boolean = privateLists.length !== 0;
    const [selectedListID, setSelectedListId] = useState<string>('');

    const onOpenListDetails = (listId: string) => {
        setSelectedListId(listId);
    }


    let privateListDetailsProps: PrivateListDetailsProps = {
        listID: selectedListID,
    }

    // Obtener listas privadas al cargar componente
    useEffect(() => {
        let onSuccess: CallbackFunction = () => {};
        let onError: CallbackFunction = () => {};
        dispatch(userRedux.actions.getPrivateListsAsyncAction(userID, onSuccess, onError));
    }, [userID])

    return (
        <Container>
            <Row>
                <Col md={4}>
                    <Row>
                        <h5>
                            <FormattedMessage id='users.components.PrivateListsPage.title' />
                        </h5>
                        <CreatePrivateList />
                    </Row>

                    <br/>

                    <Row>
                        {(hasPrivateLists) ?
                            <Col>
                                <h5>
                                    <FormattedMessage id='users.components.PrivateListsPage.myLists' />
                                </h5>
                                {privateLists.map((pl) => {
                                    let summaryProps: PrivateListSummaryProps = {
                                        privateList: pl,
                                        onOpenDetailsClick: onOpenListDetails,
                                    }

                                    return <PrivateListSummary {...summaryProps} />
                                })}
                            </Col>
                            :
                            <Alert variant="warning">
                                <FormattedMessage id="common.alerts.noResults" />
                            </Alert>
                        }
                    </Row>
                </Col>

                <Col>
                    <PrivateListDetails {...privateListDetailsProps} />
                </Col>
            </Row>
        </Container>
    )
}

export default PrivateListsPage;
