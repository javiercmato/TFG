import {useAppDispatch} from "../../store";
import {Link, useNavigate} from "react-router-dom";
import {ChangeEvent, FormEvent, useState} from "react";
import {ErrorDto, Errors} from "../../App";
import {userRedux} from "../Application";
import {Button, Card, Form, FormGroup, InputGroup, Row,} from "react-bootstrap";
import {cardHeader, encouragementText, form, formGroup, row} from "./styles/login";
import {FormattedMessage} from "react-intl";
import {FaEye, FaEyeSlash} from "react-icons/fa";

const Login = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [nickname, setNickname] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [hidePassword, setHidePassword] = useState<boolean>(true);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    let formRef: HTMLFormElement;


    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (formRef.checkValidity()) {
            let onSuccess = () => {
                navigate("/");
            }

            let onError = (error: ErrorDto) => {
                setBackendErrors(error);
            }

            let onReauthenticate = () =>{
                navigate("/login")
            }

            dispatch(userRedux.actions.loginAsyncAction(
                nickname.trim(), password, onSuccess, onError, onReauthenticate
            ))
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
                <Card.Header style={cardHeader}>
                    <h4>
                        <FormattedMessage id="users.components.Login.title" />
                    </h4>
                </Card.Header>

                <Card.Body>
                    <Form
                        ref={(node: HTMLFormElement) => {formRef = node}}
                        onSubmit={(e: FormEvent<HTMLFormElement>) => handleSubmit(e)}
                        style={form}
                    >
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
                                <InputGroup>
                                    <Form.Control as="input"
                                                  type={(hidePassword) ? 'password' : 'text'}
                                                  value={password}
                                                  onChange={(e: ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
                                    />
                                    <Button variant="outline-secondary"
                                        onClick={() => setHidePassword((prevState => !prevState))}
                                    >
                                        {(hidePassword) ? <FaEye/> : <FaEyeSlash />}
                                    </Button>
                                </InputGroup>
                                <Form.Control.Feedback
                                    type="invalid"
                                    role="alert"
                                >
                                    <FormattedMessage id="common.validation.mandatoryField" />
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        <br/>
                        {/* Botón para iniciar sesión */}
                        <Row style={row}>
                            <FormGroup style={formGroup}>
                                <Button type="submit">
                                    <FormattedMessage id="users.components.Login.loginButton"/>
                                </Button>
                            </FormGroup>
                        </Row>

                        {/* Enlace para registrarse */}
                        <Row style={row}>
                            <p style={encouragementText}>
                                <FormattedMessage id="users.components.Login.signUpEncouragementText"/>
                                <br/>
                                <Link to="/signUp">
                                    <FormattedMessage id="users.components.SignUp.signUpButton"/>
                                </Link>
                            </p>
                        </Row>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    )
}


export default Login;
