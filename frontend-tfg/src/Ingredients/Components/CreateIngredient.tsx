import {useSelector} from "react-redux";
import {userRedux} from "../../Users";
import {Button, Card, Col, Form, FormControl, Row} from "react-bootstrap";
import {card, cardHeader} from "./styles/createIngredient";
import {FormattedMessage, useIntl} from "react-intl";
import {ChangeEvent, FormEvent, useState} from "react";
import {useAppDispatch} from "../../store";
import {ingredientsRedux} from "../Application";
import IngredientTypeSelector from "./IngredientTypeSelector";


const CreateIngredient = () => {
    const intl = useIntl();
    const dispatch = useAppDispatch();
    const [name, setName] = useState<string>('');
    const [typeID, setTypeID] = useState<string>('');
    const isLoggedIn = useSelector(userRedux.selectors.isLoggedIn);
    const userID = useSelector(userRedux.selectors.selectUserID);

    const handleChangeType = (e: any) => {
        e.preventDefault();

        setTypeID(e.target.value);
    }

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let onSuccess = () => {};
        let onError = () => {};

        dispatch(ingredientsRedux.actions.createIngredientAsyncAction(name, typeID, userID, onSuccess, onError));
    }

    // Solo usuarios registrados pueden crear ingredientes
    if (!isLoggedIn) return null;

    return (
        <Card style={card}>
            <Card.Header style={cardHeader}>
                <b><FormattedMessage id="ingredients.components.CreateIngredient.title" /></b>
            </Card.Header>

            <Card.Body>
                <Form
                    onSubmit={handleSubmit}
                >
                    <Col>
                        <Row>
                            <FormControl
                                as="input"
                                type="text"
                                value={name}
                                placeholder={intl.formatMessage({id: 'ingredients.components.CreateIngredient.placeholder'})}
                                onChange={(e: ChangeEvent<HTMLInputElement>) => setName(e.target.value)}
                            />
                        </Row>

                        <br/>

                        <Row>
                            <IngredientTypeSelector onChangeCallback={handleChangeType}/>
                        </Row>

                        <br/>

                        <Row>
                            <Button type="submit">
                                <FormattedMessage id="common.buttons.create" />
                            </Button>
                        </Row>
                    </Col>
                </Form>
            </Card.Body>
        </Card>
    )
}

export default CreateIngredient;
