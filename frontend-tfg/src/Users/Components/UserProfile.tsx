import {useAppDispatch} from "../../store";
import {useNavigate, useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import {userRedux} from "../Application";
import {ErrorDto, Errors} from "../../App";
import {useEffect, useState} from "react";
import {Alert, Button, Card, Col, Row} from "react-bootstrap";
import UserAvatar from "./UserAvatar";
import {cardHeaderRow, userActionsCol, userDataCol} from './styles/userProfile';
import {FormattedMessage} from "react-intl";
import BanUserButton from "./BanUserButton";


const UserProfile = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    let {nickname} = useParams();
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const [shouldBannedUserAlert, setShowBannedUserAlert] = useState<boolean>(true);
    let isUserLoggedIn = useSelector(userRedux.selectors.isLoggedIn);
    let loggedUser = useSelector(userRedux.selectors.selectCurrentUser);
    let searchedUser = useSelector(userRedux.selectors.selectUserSearch);
    let isAdmin = useSelector(userRedux.selectors.selectIsAdmin);
    let isSearchedUserBannedByAdmin = useSelector(userRedux.selectors.isUserSearchBannedByAdmin);
    const isCurrentUserProfile : boolean = (isUserLoggedIn && (nickname === loggedUser?.nickname));


    // Solicita los datos del usuario cada vez que cambie el nickname en la URL
    useEffect( () => {
        let onSuccess = () => {};

        let onError = (error: ErrorDto) => {
            setBackendErrors(error);
        }

        dispatch(userRedux.actions.findUserByNicknameAsyncAction(nickname!, onSuccess, onError));
    }, [nickname]);


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

                            {/* Seguidores y botones */}
                            <Col style={userActionsCol}>
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
