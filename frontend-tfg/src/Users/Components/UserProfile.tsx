import {useAppDispatch} from "../../store";
import {useNavigate, useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import {userRedux} from "../Application";
import {ErrorDto, Errors} from "../../App";
import {useEffect, useState} from "react";
import {Button, Card, Col, Row} from "react-bootstrap";
import UserAvatar from "./UserAvatar";
import {cardHeaderRow, userActionsCol, userDataCol} from './styles/userProfile';
import {FormattedMessage} from "react-intl";


const UserProfile = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    let {nickname} = useParams();
    let isUserLoggedIn = useSelector(userRedux.selectors.isLoggedIn);
    let loggedUser = useSelector(userRedux.selectors.selectCurrentUser);
    let profileData = useSelector(userRedux.selectors.selectUserSearch);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);

    const isCurrentUserProfile = (isUserLoggedIn && (nickname ==loggedUser?.nickname));

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
            {(profileData) &&
                <Card
                    bg="light"
                    border="light"
                >
                    {/* Datos del usuario */}
                    <Card.Header>
                        <Row style={cardHeaderRow}>
                            {/* Avatar */}
                            <Col>
                                {(profileData) &&
                                    <UserAvatar
                                        imageB64={profileData.avatar}
                                        userNickname={profileData.nickname!}
                                        isThumbnail={false}
                                    />
                                }
                            </Col>

                            {/* Información del usuario */}
                            <Col style={userDataCol}>
                                {/* Nombre */}
                                <Row>
                                    <h3>{profileData.name + ' ' + profileData.surname}</h3>
                                </Row>

                                {/* Nickname */}
                                <Row>
                                    <h4>{profileData.nickname}</h4>
                                </Row>

                                {/* Email */}
                                <Row>
                                    <div>{profileData.email}</div>
                                </Row>
                            </Col>

                            {/* Seguidores y botones */}
                            <Col style={userActionsCol}>
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
