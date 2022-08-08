import {useAppDispatch} from "../../store";
import {ChangeEvent, FormEvent, useState} from "react";
import {ErrorDto} from "../../App";
import {Button, Card, Form, FormControl, FormGroup, InputGroup} from "react-bootstrap";
import {useSelector} from "react-redux";
import {userRedux} from "../../Users";
import {FormattedMessage, useIntl} from "react-intl";
import {cardHeader} from "./styles/createIngredientType";
import {FaPlus} from "react-icons/fa";
import {ingredientsRedux} from "../Application";

const CreateIngredientType = () => {
    const dispatch = useAppDispatch();
    const intl = useIntl();
    const [name, setName] = useState<string>('');
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const isCurrentUserAdmin = useSelector(userRedux.selectors.selectIsAdmin);

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        let onSuccess = () => {}
        let onError = (error: ErrorDto) => {
            setBackendErrors(error);
        }

        dispatch(ingredientsRedux.actions.createIngredientTypeAsyncAction(name, onSuccess, onError));
    }

    // Solo un usuario administrador puede crear nuevos tipos de ingrediente
    if (!isCurrentUserAdmin) return null;

    return (
        <Card>
            <Card.Header style={cardHeader}>
                <b><FormattedMessage id="ingredients.components.CreateIngredientType.title" /></b>
            </Card.Header>

            <Card.Body>
                <Form
                    onSubmit={handleSubmit}
                >
                    <FormGroup>
                        <InputGroup>
                            <FormControl
                                as="input"
                                type="text"
                                value={name}
                                placeholder={intl.formatMessage({id: 'ingredients.components.CreateIngredientType.placeholder'})}
                                onChange={(e: ChangeEvent<HTMLInputElement>) => setName(e.target.value)}
                            />
                            <Button
                                type="submit"
                                variant="outline-primary"
                            >
                                <FaPlus />
                            </Button>
                        </InputGroup>
                    </FormGroup>
                </Form>
            </Card.Body>
        </Card>
    )
}

export default CreateIngredientType;
