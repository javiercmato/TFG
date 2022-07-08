import {useAppDispatch} from "../../store";
import {useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import {userRedux} from "../Application";
import {ErrorDto, Errors} from "../../App";
import {useEffect, useState} from "react";
import {Card, Col, Row} from "react-bootstrap";
import UserAvatar from "./UserAvatar";
import {userDataCol} from './styles/userProfile';


const UserProfile = () => {
    const dispatch = useAppDispatch();
    let {nickname} = useParams();
    let searchedUser = useSelector(userRedux.selectors.selectUserSearch);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);


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
                    {/* Datos del usuario */}
                    <Card.Header>
                        <Row>
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
                            <Col></Col>
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
