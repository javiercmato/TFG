import {ErrorDto, Errors, UploadFileButton} from "../../App";
import {ChangeEvent, createRef, FormEvent, useState} from "react";
import {Button, Card, Form, FormGroup, Image, InputGroup, Row} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FormattedMessage} from "react-intl";
import {avatarPreview, cardHeader, form, formGroup, row} from "./styles/signUp";
import {useAppDispatch} from "../../store";
import {useNavigate} from "react-router-dom";
import {userRedux} from "../Application";
import {User} from "../Domain";
import {FaEye, FaEyeSlash} from "react-icons/fa";


const SignUp = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();             // Antiguo hook useHistory()
    const [name, setName] = useState<string>('');
    const [surname, setSurname] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [nickname, setNickname] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setconfirmPassword] = useState<string>('');
    const [hideConfirmPassword, setHideConfirmPassword] = useState<boolean>(true);
    const [passwordsDoNotMatch, setPasswordsDoNotMatch] = useState<boolean>(false);
    // @ts-ignore
    const [avatar, setAvatar] = useState<string>(null);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    let formRef: HTMLFormElement;
    let confirmPasswordInputRef = createRef<HTMLInputElement>();


    const handleConfirmPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        confirmPasswordInputRef.current?.setCustomValidity('');
        setconfirmPassword(e.target.value);
        setPasswordsDoNotMatch(false);
    }


    const handleAvatarUpload = (file: File) => {
        // Convertir imagen a string: https://stackoverflow.com/a/42498790/11295728
        let reader = new FileReader();
        reader.onloadend = () => {
            let dataURL = reader.result as string;
            setAvatar(dataURL);
        }
        reader.readAsDataURL(file);
    }

    const checkConfirmPassword = () => {
        if (password !== confirmPassword){      // Contraseñas no coinciden
            confirmPasswordInputRef.current?.setCustomValidity('');
            setPasswordsDoNotMatch(true);

            return false;
        } else {
            return true;
        }
    }

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (formRef.checkValidity() && checkConfirmPassword()) {
            let onSuccess = () => {
                navigate("/");
            }

            let onError = (error: ErrorDto) => {
                setBackendErrors(error);
            }

            let onReauthenticate = () =>{
                navigate("/login")
            }

            let user: User = {
                name: name.trim(),
                surname: surname.trim(),
                email: email.trim(),
                nickname: nickname.trim(),
                password: password,
                avatar: avatar?.split(",")[1],
            };

            dispatch(userRedux.actions.signUpAsyncAction(
                user, onSuccess, onError, onReauthenticate
            ));
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
                        <FormattedMessage id="users.components.SignUp.title" />
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

                        {/* Nickname */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.nickname" /></b>
                                </Form.Label>
                                <Form.Control as="input"
                                              type="text"
                                              value={nickname}
                                              onChange={(e: ChangeEvent<HTMLInputElement>) => setNickname(e.target.value)}
                                />
                                <Form.Control.Feedback
                                    type="invalid"
                                    role="alert"
                                >
                                    <FormattedMessage id="common.validation.mandatoryField" />
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        {/* Contraseña */}
                        <Row style={row}>
                            <FormGroup>

                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.password" /></b>
                                </Form.Label>
                                <Form.Control as="input"
                                              type="text"
                                              value={password}
                                              onChange={(e: ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
                                />
                                <Form.Control.Feedback
                                    type="invalid"
                                    role="alert"
                                >
                                    <FormattedMessage id="common.validation.mandatoryField" />
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        {/* Confirmar contraseña */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.confirmPassword" /></b>
                                </Form.Label>
                                <InputGroup>
                                    <Form.Control as="input"
                                       ref={confirmPasswordInputRef}
                                       type={(hideConfirmPassword) ? 'password' : 'text'}
                                       value={confirmPassword}
                                       onChange={(e: ChangeEvent<HTMLInputElement>) => handleConfirmPasswordChange(e)}
                                    />
                                    <Button variant="outline-secondary"
                                        onClick={() => setHideConfirmPassword((prevState => !prevState))}
                                    >
                                        {(hideConfirmPassword) ? <FaEye/> : <FaEyeSlash/>}
                                    </Button>
                                </InputGroup>
                                <Form.Control.Feedback
                                    type="invalid"
                                    role="alert"
                                >
                                    {(passwordsDoNotMatch) ?
                                        <FormattedMessage id="common.validation.passwordsDoNotMatch"/>
                                        : <FormattedMessage id="common.validation.mandatoryField"/>
                                    }
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        <br/>
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
                        {/* Botón para registrar */}
                        <Row style={row}>
                            <FormGroup style={formGroup}>
                                <Button type="submit">
                                    <FormattedMessage id="users.components.SignUp.signUpButton"/>
                                </Button>
                            </FormGroup>
                        </Row>
                    </Form>
                </Card.Body>

            </Card>
        </div>
    )
};



export default SignUp;
