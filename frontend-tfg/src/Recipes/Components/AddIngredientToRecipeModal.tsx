import {Alert, Button, Col, Container, FormControl, FormGroup, FormLabel, Modal, Row} from "react-bootstrap";
import {useState} from "react";
import {FormattedMessage, useIntl} from "react-intl";
import {Ingredient, MeasureUnitSelector, MeasureUnitSelectorProps} from "../../Ingredients";
import {useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {modalHeader} from "./styles/addIngredientToRecipeModal";
import {CreateRecipeIngredientParamsDTO} from "../Infrastructure";


interface Props {
    show: boolean,
    ingredient?: Ingredient,
    onHideCallback: any,
    onAddIngredientCallback: any,
}

const AddIngredientToRecipeModal = ({show, ingredient, onHideCallback, onAddIngredientCallback}: Props) => {
    const intl = useIntl();
    const [quantity, setQuantity] = useState<string>('');
    const [measureUnit, setMeasureUnit] = useState<string>('');
    const [recipeIngredientParams, setRecipeIngredientParams] = useState<CreateRecipeIngredientParamsDTO>();
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);



    let measureUnitSelectorProps: MeasureUnitSelectorProps = {
        onChangeCallback: (e: any) => setMeasureUnit(e.target.value),
    }

    const onAddIngredientClick = () => {
        let params: CreateRecipeIngredientParamsDTO = {
            ingredientID: ingredient!.id,
            quantity: quantity,
            measureUnit: measureUnit
        }
        setRecipeIngredientParams(params);
        onAddIngredientCallback(params);
    }

    return (
        <Modal
            show={show}
            onHide={onHideCallback}
            style={modalHeader}
        >
            <Modal.Header closeButton>
                <FormattedMessage id="recipes.components.AddIngredientToRecipeModal.title" />
            </Modal.Header>

            <Modal.Body>
                {/* Si está logeado muestra formulario. Sino avisa de que no puede realizar la operación */}
                {(isLoggedIn) ?
                    <Container>
                        <Col>
                            <Row>
                                <h4>{ingredient?.name}</h4>
                            </Row>

                            {/* Cantidad del ingrediente */}
                            <Row>
                                <FormGroup>
                                    <FormLabel>
                                        <FormattedMessage id="common.fields.quantity" />
                                    </FormLabel>
                                    <FormControl
                                        as="input"
                                        value={quantity}
                                        placeholder={intl.formatMessage({id: 'recipes.components.AddIngredientToRecipeModal.quantity.placeholder'})}
                                        onChange={(e: any) => setQuantity(e.target.value)}
                                    />
                                </FormGroup>
                            </Row>

                            <br/>

                            {/* Unidad de medida del ingrediente */}
                            <Row>
                                <FormGroup>
                                    <FormLabel>
                                        <FormattedMessage id="common.fields.measureUnit" />
                                    </FormLabel>
                                    <MeasureUnitSelector {...measureUnitSelectorProps} />
                                </FormGroup>
                            </Row>
                        </Col>
                    </Container>
                :
                    <Alert variant="danger">
                        <FormattedMessage id="common.alerts.notLoggedIn" />
                    </Alert>
                }
            </Modal.Body>

            <Modal.Footer>
                <Button onClick={onAddIngredientClick}>
                    <FormattedMessage id="common.buttons.add" />
                </Button>
            </Modal.Footer>
        </Modal>
    )
}


export type {Props as AddIngredientToRecipeModalProps};
export default AddIngredientToRecipeModal;
