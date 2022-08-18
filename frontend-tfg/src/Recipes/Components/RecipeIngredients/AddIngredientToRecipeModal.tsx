import {Alert, Button, Col, Container, FormControl, FormGroup, FormLabel, Modal, Row} from "react-bootstrap";
import {useState} from "react";
import {FormattedMessage, useIntl} from "react-intl";
import {Ingredient, MeasureUnitSelector, MeasureUnitSelectorProps} from "../../../Ingredients";
import {useAppSelector} from "../../../store";
import {userRedux} from "../../../Users";
import {modalHeader} from "../styles/addIngredientToRecipeModal";
import {CreateRecipeIngredientParamsDTO} from "../../Infrastructure";


interface Props {
    show: boolean,
    setShow: any,
    ingredient: Nullable<Ingredient>,
    onHideCallback: any,
    onAddCustomizedIngredient: (ingredientName: string, params: CreateRecipeIngredientParamsDTO) => void,
}

const AddIngredientToRecipeModal = ({show, setShow, ingredient, onHideCallback, onAddCustomizedIngredient}: Props) => {
    const intl = useIntl();
    const [quantity, setQuantity] = useState<string>('');
    const [measureUnit, setMeasureUnit] = useState<string>('');
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);


    const handleAddClick = (e: any) => {
        e.preventDefault();

        // Crea los parámetros para añadir ingrediente a la receta y se los pasa al padre
        let params: CreateRecipeIngredientParamsDTO = {
            ingredientID: ingredient?.id!,
            quantity: quantity,
            measureUnit: measureUnit,
        }
        onAddCustomizedIngredient(ingredient?.name!, params);                   // Devuelve los parámetros del ingrediente al padre
        setShow(false);                                                         // Cierra el modal
    }

    let measureUnitSelectorProps: MeasureUnitSelectorProps = {
        onChangeCallback: (e: any) => setMeasureUnit(e.target.value),
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
                {/* Si está logeado muestra botón para añadir ingrediente */}
                {(isLoggedIn) &&
                    <Button onClick={(e) => handleAddClick(e)}>
                        <FormattedMessage id="common.buttons.add" />
                    </Button>
                }
            </Modal.Footer>
        </Modal>
    )
}


export type {Props as AddIngredientToRecipeModalProps};
export default AddIngredientToRecipeModal;
