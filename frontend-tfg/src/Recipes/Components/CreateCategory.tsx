import {useAppDispatch, useAppSelector} from "../../store";
import {FormattedMessage, useIntl} from "react-intl";
import {ChangeEvent, FormEvent, useState} from "react";
import {userRedux} from "../../Users";
import {ErrorDto} from "../../App";
import {Button, Card, Form, FormControl, FormGroup, InputGroup} from "react-bootstrap";
import {cardHeader} from "../../Ingredients/Components/styles/createIngredientType";
import {FaPlus} from "react-icons/fa";
import {recipesRedux} from "../Application";
import {CreateCategoryParamsDTO} from "../Infrastructure";

const CreateCategory = () => {
    const dispatch = useAppDispatch();
    const intl = useIntl();
    const [name, setName] = useState<string>('');
    // @ts-ignore
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const isCurrentUserAdmin = useAppSelector(userRedux.selectors.selectIsAdmin);

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        let onSuccess = () => {}
        let onError = (error: ErrorDto) => {
            setBackendErrors(error);
        }
        let params: CreateCategoryParamsDTO = {
            name: name,
        }
        dispatch(recipesRedux.actions.createCategoryAsyncAction(params, onSuccess, onError));
    }


    // Solo un usuario administrador puede crear nuevas categorías
    if (!isCurrentUserAdmin) return null;

    return (
        <Card>
            <Card.Header style={cardHeader}>
                <b><FormattedMessage id="recipes.components.CreateCategory.title" /></b>
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
                                placeholder={intl.formatMessage({id: 'recipes.components.CreateCategory.placeholder'})}
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


export default CreateCategory;
