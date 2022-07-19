import {useAppDispatch, useAppSelector} from "../../store";
import {useNavigate} from "react-router-dom";
import {ChangeEvent, createRef, FormEvent, useState} from "react";
import {ErrorDto, Errors} from "../../App";
import {userRedux} from "../Application";
import {Button, Card, Form, FormGroup, InputGroup, Row} from "react-bootstrap";
import {cardHeader, form, formGroup, row} from "./styles/signUp";
import {FormattedMessage} from "react-intl";
import {FaEye, FaEyeSlash} from "react-icons/fa";

const ChangePassword = () => {
    const dispatch = useAppDispatch();
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const navigate = useNavigate();
    const [oldPassword, setOldPassword] = useState<string>('');
    const [newPassword, setNewPassword] = useState<string>('');
    const [confirmNewPassword, setConfirmNewPassword] = useState<string>('');
    const [passwordsDoNotMatch, setPasswordsDoNotMatch] = useState<boolean>(false);
    const [hideOldPassword, setHideOldPassword] = useState<boolean>(false);
    const [hideNewPassword, setHideNewPassword] = useState<boolean>(true);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    let formRef: HTMLFormElement;
    let confirmNewPasswordInputRef = createRef<HTMLInputElement>();


    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (formRef.checkValidity() && checkConfirmNewPassword()) {
            let onSuccess = () => {
                navigate("/");
            }

            let onError = (error: ErrorDto) => {
                setBackendErrors(error);
            }

            dispatch(userRedux.actions.changePasswordAsyncAction(
                userID, oldPassword, newPassword, onSuccess, onError
            ))
        } else {
            setBackendErrors(null);
            formRef.classList.add('was-validated');
        }
    }

    const checkConfirmNewPassword = () => {
        if (newPassword !== confirmNewPassword){      // Contraseñas no coinciden
            confirmNewPasswordInputRef.current?.setCustomValidity('error');
            setPasswordsDoNotMatch(true);

            return false;
        } else {
            return true;
        }
    }

    const handleConfirmNewPasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
        confirmNewPasswordInputRef.current?.setCustomValidity('');
        setConfirmNewPassword(e.target.value);
        setPasswordsDoNotMatch(false);
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
                        <FormattedMessage id="users.components.ChangePassword.title" />
                    </h4>
                </Card.Header>

                <Card.Body>
                    <Form
                        ref={(node: HTMLFormElement) => {formRef = node}}
                        onSubmit={(e: FormEvent<HTMLFormElement>) => handleSubmit(e)}
                        style={form}
                    >
                        {/* Antigua contraseña */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="users.components.ChangePassword.oldPassword" /></b>
                                </Form.Label>
                                <InputGroup>
                                    <Form.Control as="input"
                                                  type={(hideOldPassword) ? 'password' : 'text'}
                                                  value={oldPassword}
                                                  onChange={(e: ChangeEvent<HTMLInputElement>) => setOldPassword(e.target.value)}
                                    />
                                    <Button variant="outline-secondary"
                                            onClick={() => setHideOldPassword((prevState => !prevState))}
                                    >
                                        {(hideOldPassword) ? <FaEye/> : <FaEyeSlash />}
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

                        {/* Nueva contraseña */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="users.components.ChangePassword.newPassword" /></b>
                                </Form.Label>
                                <InputGroup>
                                    <Form.Control as="input"
                                                  type={(hideNewPassword) ? 'password' : 'text'}
                                                  value={newPassword}
                                                  onChange={(e: ChangeEvent<HTMLInputElement>) => setNewPassword(e.target.value)}
                                    />
                                    <Button variant="outline-secondary"
                                            onClick={() => setHideNewPassword((prevState => !prevState))}
                                    >
                                        {(hideNewPassword) ? <FaEye/> : <FaEyeSlash />}
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

                        {/* Confirmar contraseña */}
                        <Row style={row}>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="users.components.ChangePassword.confirmNewPassword" /></b>
                                </Form.Label>
                                <InputGroup>
                                    <Form.Control as="input"
                                                  type={(hideNewPassword) ? 'password' : 'text'}
                                                  value={confirmNewPassword}
                                                  onChange={(e: ChangeEvent<HTMLInputElement>) => handleConfirmNewPasswordChange(e)}
                                    />
                                    <Button variant="outline-secondary"
                                            onClick={() => setHideNewPassword((prevState => !prevState))}
                                    >
                                        {(hideNewPassword) ? <FaEye/> : <FaEyeSlash />}
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
                        {/* Botón para cambiar contraseña */}
                        <Row style={row}>
                            <FormGroup style={formGroup}>
                                <Button type="submit">
                                    <FormattedMessage id="users.components.ChangePassword.changePassword"/>
                                </Button>
                            </FormGroup>
                        </Row>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    )
}

export default ChangePassword;
