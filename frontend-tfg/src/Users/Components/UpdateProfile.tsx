import {useAppDispatch} from "../../store";
import {useNavigate} from "react-router-dom";
import {ChangeEvent, FormEvent, useState} from "react";
import {ErrorDto, Errors, UploadFileButton} from "../../App";
import {Button, Card, Form, FormGroup, Image, Row} from "react-bootstrap";
import {cardHeader} from "./styles/updateProfile";
import {FormattedMessage} from "react-intl";
import CardHeader from "react-bootstrap/CardHeader";
import {avatarPreview, form, formGroup, row} from "./styles/signUp";
import {useSelector} from "react-redux";
import {userRedux} from "../Application";
import {User} from "../Domain";

const UpdateProfile = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const currentUser = useSelector(userRedux.selectors.selectCurrentUser);
    const [name, setName] = useState<string>(currentUser?.name!);
    const [surname, setSurname] = useState<string>(currentUser?.surname!);
    const [email, setEmail] = useState<string>(currentUser?.email!);
    const [avatar, setAvatar] = useState<Nullable<string>>(currentUser?.avatar!);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    let formRef: HTMLFormElement;

    const handleAvatarUpload = (file: File) => {
        // Convertir imagen a string: https://stackoverflow.com/a/42498790/11295728
        let reader = new FileReader();
        reader.onloadend = () => {
            let dataURL = reader.result as string;
            setAvatar(dataURL);
        }
        reader.readAsDataURL(file);
    }

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (formRef.checkValidity()) {
            let onSuccess = () => {
                navigate(`/users/${currentUser?.nickname!}`);
            };

            let onError = (error: ErrorDto) => {
                setBackendErrors(error);
            }

            let updatedUser: User = {
                name: name.trim(),
                surname: surname.trim(),
                email: email.trim(),
                avatar: avatar?.split(",")[1],
            };

            dispatch(userRedux.actions.updateProfileAsyncAction(currentUser?.userID!, updatedUser, onSuccess, onError));
        } else {
            setBackendErrors(null);
            formRef.classList.add('was-validated');
        }
    }


    return (
        <div>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            <Card
                bg="light"
                border="light"
            >
                <CardHeader style={cardHeader}>
                    <h4>
                        <FormattedMessage id="users.components.UpdateProfile.title" />
                    </h4>
                </CardHeader>

                <Card.Body>
                    <Form
                        ref={(node: HTMLFormElement) => {formRef = node}}
                        onSubmit={(e: FormEvent<HTMLFormElement>) => handleSubmit(e)}
                        style={form}
                    >
                        {/* Nombre */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.name" /></b>
                                </Form.Label>
                                <Form.Control as="input"
                                              type="text"
                                              value={name}
                                              onChange={(e: ChangeEvent<HTMLInputElement>) => setName(e.target.value)}
                                />
                                <Form.Control.Feedback
                                    type="invalid"
                                    role="alert"
                                >
                                    <FormattedMessage id="common.validation.mandatoryField" />
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        {/* Apellidos */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.surname" /></b>
                                </Form.Label>
                                <Form.Control as="input"
                                              type="text"
                                              value={surname}
                                              onChange={(e: ChangeEvent<HTMLInputElement>) => setSurname(e.target.value)}
                                />
                                <Form.Control.Feedback
                                    type="invalid"
                                    role="alert"
                                >
                                    <FormattedMessage id="common.validation.mandatoryField" />
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        {/* Email */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.email" /></b>
                                </Form.Label>
                                <Form.Control as="input"
                                              type="email"
                                              value={email}
                                              onChange={(e: ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)}
                                />
                                <Form.Control.Feedback
                                    type="invalid"
                                    role="alert"
                                >
                                    <FormattedMessage id="common.validation.email" />
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        {/* Avatar */}
                        <Row style={row}>
                            <FormGroup style={formGroup}>
                                {(avatar) &&
                                    <Image
                                        thumbnail
                                        src={avatar}
                                        style={avatarPreview}
                                    />
                                }
                                <br/>
                                <UploadFileButton
                                    fileTipe="image/*"
                                    onUploadCallback={handleAvatarUpload}
                                />
                            </FormGroup>
                        </Row>

                        <br/>
                        {/* Botón para actualizar perfil */}
                        <Row style={row}>
                            <FormGroup style={formGroup}>
                                <Button type="submit">
                                    <FormattedMessage id="users.components.UpdateProfile.updateButton"/>
                                </Button>
                            </FormGroup>
                        </Row>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    )
}

export default UpdateProfile;
