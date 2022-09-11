import {Alert, Col, Container, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../../store";
import {socialRedux} from "../Application";
import {useLayoutEffect, useState} from "react";
import {SearchCriteria} from "../../App";
import searchCriteria from "../../App/Domain/common/SearchCriteria";
import {Link, useParams} from "react-router-dom";
import {FormattedMessage} from "react-intl";

const FollowersPage = () => {
    const dispatch = useAppDispatch();
    const {userID} = useParams();
    const followers = useAppSelector(socialRedux.selectors.getFollowers).result;
    const followings = useAppSelector(socialRedux.selectors.getFollowings).result;
    const [followersPage, setFollowersPage] = useState<number>(0);
    const [followingsPage, setFollowingsPage] = useState<number>(0);


    // Cargar seguidores
    useLayoutEffect(() => {
        // Cargar seguidores
        let followersCriteria: SearchCriteria = {
            ...searchCriteria,
            userID: userID!,
            page: followersPage
        }
        let onSuccessFollowers = () => {};
        let onErrorFollowers = () => {};
        dispatch(socialRedux.actions.getFollowersAsyncAction(followersCriteria, onSuccessFollowers, onErrorFollowers));

    }, [userID, followersPage]);

    // Buscar siguiendos
    useLayoutEffect(() => {
        let followingsCriteria: SearchCriteria = {
            ...searchCriteria,
            userID: userID!,
            page: followingsPage
        }
        let onSuccessFollowings = () => {};
        let onErrorFollowings = () => {};
        dispatch(socialRedux.actions.getFollowingsAsyncAction(followingsCriteria, onSuccessFollowings, onErrorFollowings));
    }, [userID, followingsPage]);




    return (
        <Container>
            <Row>
                <Col>
                    <Row>
                        <h4>
                            <FormattedMessage id="social.components.FollowersPage.followedBy"/>
                        </h4>
                    </Row>

                    <Row>
                        {(followers?.itemsCount === 0) ?
                            <Alert variant="info">
                                <FormattedMessage id="common.alerts.noResults" />
                            </Alert>
                            :
                            <Col>
                                <ListGroup>
                                    {followers?.items?.map((follow, index) =>
                                        <ListGroupItem>
                                            <Container>
                                                <Row>
                                                    <Col>
                                                        <Link to={`/users/${follow.followed.userID}`}>
                                                            {follow.followed.nickname}
                                                        </Link>
                                                    </Col>

                                                    <Col>
                                                        {follow.followed.name}
                                                    </Col>
                                                </Row>
                                            </Container>
                                        </ListGroupItem>
                                    )}
                                </ListGroup>
                            </Col>
                        }
                    </Row>
                </Col>

                <Col>
                    <Row>
                        <h4>
                            <FormattedMessage id="social.components.FollowersPage.following"/>
                        </h4>
                    </Row>

                    <Row>
                        {(followings?.itemsCount === 0) ?
                            <Alert variant="info">
                                <FormattedMessage id="common.alerts.noResults" />
                            </Alert>
                            :
                            <Col>
                                <ListGroup>
                                    {followings?.items?.map((follow, index) =>
                                        <ListGroupItem>
                                            <Container>
                                                <Row>
                                                    <Col>
                                                        <Link to={`/users/${follow.followed.userID}`}>
                                                            {follow.followed.nickname}
                                                        </Link>
                                                    </Col>

                                                    <Col>
                                                        {follow.followed.name}
                                                    </Col>
                                                </Row>
                                            </Container>
                                        </ListGroupItem>
                                    )}
                                </ListGroup>
                            </Col>
                        }
                    </Row>
                </Col>
            </Row>
        </Container>
    )
}

export default FollowersPage;

