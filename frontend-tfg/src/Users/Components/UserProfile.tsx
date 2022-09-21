import {useAppDispatch} from "../../store";
import {useNavigate, useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import {userRedux} from "../Application";
import {ErrorDto, Errors} from "../../App";
import React, {MouseEventHandler, useEffect, useState} from "react";
import {Alert, Button, Card, Col, Row} from "react-bootstrap";
import UserAvatar from "./UserAvatar";
import {cardHeaderRow, userActionsCol, userDataCol} from './styles/userProfile';
import {FormattedMessage} from "react-intl";
import BanUserButton from "./BanUserButton";
import {FollowButton, FollowButtonProps, socialRedux, UnfollowButton, UnfollowButtonProps} from "../../Social";
import {User} from "../Domain";


const UserProfile = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    let {userID} = useParams();
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const [shouldBannedUserAlert, setShowBannedUserAlert] = useState<boolean>(true);
    const [targetUserID, setTargetUserID] = useState<string>('');
    let isUserLoggedIn = useSelector(userRedux.selectors.isLoggedIn);
    let loggedUser = useSelector(userRedux.selectors.selectCurrentUser);
    let currentUserID = useSelector(userRedux.selectors.selectUserID);
    let searchedUser = useSelector(userRedux.selectors.selectUserSearch);
    let isAdmin = useSelector(userRedux.selectors.selectIsAdmin);
    let isSearchedUserBannedByAdmin = useSelector(userRedux.selectors.isUserSearchBannedByAdmin);
    let isUserSearchFollowedByUser = useSelector(userRedux.selectors.isUserSearchFollowedByUser);
    const isCurrentUserProfile : boolean = (isUserLoggedIn && (userID === loggedUser?.userID));


    const handleClickDeleteUser : MouseEventHandler<HTMLButtonElement> = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        let onSuccess: NoArgsCallbackFunction = () => {
            navigate("/");
            dispatch(userRedux.actions.logoutAsyncAction());
        };
        let onErrors: CallbackFunction = (err) => {setBackendErrors(err)};

        dispatch(userRedux.actions.deleteUserAsyncAction(currentUserID, onSuccess, onErrors));
    }


    // Solicita los datos del usuario cada vez que cambie el nickname en la URL
    useEffect( () => {
        let onSuccess = (targetUser: User) => {
            setTargetUserID(targetUser.userID!);
        };
        let onError = (error: ErrorDto) => {
            setBackendErrors(error);
        }

            dispatch(socialRedux.actions.checkUserFollowsTargetAsyncAction(
                currentUserID, targetUserID, () => {}, () => {})
            );
        dispatch(userRedux.actions.findUserByIDAsyncAction(userID!, onSuccess, onError));

        // Libera del store los datos del perfil visitado
        return () => {
            dispatch(userRedux.actions.clearUserDetailsAction());
        }
    }, [userID, targetUserID, dispatch]);



    let followButtonProps: FollowButtonProps = {
        targetUserID: targetUserID,
        onErrorCallback: (error: ErrorDto) => setBackendErrors(error),
        onFollowCallback: () => {
            dispatch(socialRedux.actions.checkUserFollowsTargetAsyncAction(
                currentUserID, targetUserID, () => {}, () => {})
            );
        },
    }

    let unfollowButtonProps: UnfollowButtonProps = {
        targetUserID: targetUserID,
        onErrorCallback: (error: ErrorDto) => setBackendErrors(error),
        onUnfollowCallback: () => {
            dispatch(socialRedux.actions.checkUserFollowsTargetAsyncAction(
                currentUserID, targetUserID, () => {}, () => {})
            );
        },
    }

    return (
        <div>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            {(searchedUser) &&
                <Card
                    bg="light"
                    border="light"
                >
                    {/* Mostrar alerta si usuario está baneado */}
                    {(isSearchedUserBannedByAdmin && shouldBannedUserAlert) &&
                        <Alert variant="warning"
                            dismissible
                            onClose={() => setShowBannedUserAlert(false)}
                        >
                            <FormattedMessage id="users.warning.UserIsBannedByAdmin"/>
                        </Alert>
                    }

                    {/* Datos del usuario */}
                    <Card.Header>
                        <Row style={cardHeaderRow}>
                            {/* Avatar */}
                            <Col>
                                {(searchedUser) &&
                                    <UserAvatar
                                        imageB64={searchedUser.avatar}
                                        userNickname={searchedUser.nickname!}
                                        isThumbnail={false}
                                    />
                                }
                            </Col>

                            {/* Información del usuario */}
                            <Col style={userDataCol}>
                                {/* Nombre */}
                                <Row>
                                    <h3>{searchedUser.name + ' ' + searchedUser.surname}</h3>
                                </Row>

                                {/* Nickname */}
                                <Row>
                                    <h4>{searchedUser.nickname}</h4>
                                </Row>

                                {/* Email */}
                                <Row>
                                    <div>{searchedUser.email}</div>
                                </Row>
                            </Col>

                            {/* Seguir usuario */}
                            <Col md={2}>
                                {(!isCurrentUserProfile && isUserLoggedIn) &&
                                    <Row>
                                        {(isUserSearchFollowedByUser) ?
                                            <UnfollowButton {...unfollowButtonProps} />
                                            :
                                            <FollowButton {...followButtonProps} />
                                        }
                                    </Row>
                                }

                                {/* Botón para ver seguidores*/}
                                <Row>
                                    <Button
                                        onClick={() => navigate(`/users/${userID}/followers`)}
                                    >
                                        <FormattedMessage id='social.components.Followers.button' />
                                    </Button>
                                </Row>
                            </Col>

                            {/* Botones */}
                            <Col md={2} style={userActionsCol}>
                                {/* Botón para editar perfil */}
                                <Row>
                                    {(isCurrentUserProfile) &&
                                        <Button
                                            variant="secondary"
                                            onClick={() => navigate("/profile")}
                                        >
                                            <FormattedMessage id="common.buttons.edit"/>
                                        </Button>
                                    }
                                </Row>

                                {/* Botón para borrar cuenta */}
                                <Row>
                                    {(isCurrentUserProfile) &&
                                        <Button
                                            variant="danger"
                                            onClick={handleClickDeleteUser}
                                        >
                                            <FormattedMessage id="users.components.UserProfile.deleteUser"/>
                                        </Button>
                                    }
                                </Row>

                                {/* Botón para banear usuario (solo visible para administradores) */}
                                <Row>
                                    {(isAdmin && !isCurrentUserProfile) &&
                                        <BanUserButton
                                            onErrorCallback={setBackendErrors}
                                        />
                                    }
                                </Row>
                            </Col>
                        </Row>
                    </Card.Header>

                    {/* Contenido del perfil */}
                    <Card.Body>

                    </Card.Body>

                </Card>
            }
        </div>
    )
};

export default UserProfile;
