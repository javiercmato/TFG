import {FormattedMessage, useIntl} from "react-intl";
import {useAppDispatch, useAppSelector} from "../../../store";
import {ChangeEvent, FormEvent, useState} from "react";
import {Alert, Button, Col, FormControl, InputGroup, Modal, Row} from "react-bootstrap";
import {FaSearch} from "react-icons/fa";
import CreateIngredient from "../../../Ingredients/Components/CreateIngredient";
import {findRecipeIngredientTitle} from "../styles/findRecipeIngredient";
import {SearchCriteria} from "../../../App";
import {ingredientsRedux} from "../../../Ingredients";
import {userRedux} from "../../../Users";

const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

interface Props {

}

const FindRecipeIngredient = ({} : Props) => {
    const dispatch = useAppDispatch();
    const intl = useIntl();
    const [queryName, setQueryName] = useState<string>('');         // Nombre que se usará para la búsqueda
    const [showModal, setShowModal] = useState<boolean>(false);     // Mostrar el modal para crear ingrediente
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);

    const handleSearchClick = (e: FormEvent<HTMLButtonElement>) => {
        e.preventDefault();

        let criteria: SearchCriteria = {
            page: 0,
            pageSize: DEFAULT_PAGE_SIZE,
            name: (queryName !== '') ? queryName : null,
            type: null
        }

        let onSuccess = () => {};
        let hasSearchCriteria: boolean = (queryName !== '');
        let action = (hasSearchCriteria) ?
            ingredientsRedux.actions.findIngredientsAsyncAction(criteria, onSuccess)
            : ingredientsRedux.actions.findAllIngredientsAsyncAction(criteria, onSuccess);
        dispatch(action);
    }

    return (
        <>
            <Row style={findRecipeIngredientTitle}>
                <h4><FormattedMessage id="common.fields.ingredients" /></h4>

                <Col>
                    <InputGroup>
                        <FormControl
                            as="input"
                            type="text"
                            value={queryName}
                            placeholder={intl.formatMessage({id: 'ingredients.components.CreateIngredient.placeholder'})}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => setQueryName(e.target.value)}
                        />

                        <Button onClick={handleSearchClick}>
                            <FaSearch />
                        </Button>
                    </InputGroup>
                </Col>

                <Col>
                    <Button onClick={() => setShowModal(true)}>
                        <FormattedMessage id="ingredients.components.CreateIngredient.title" />
                    </Button>
                </Col>
            </Row>

            {/* Modal que aparece al hacer click en crear nuevo ingrediente */}
            <Modal show={showModal} onHide={() => setShowModal(false)} >
                <Modal.Header closeButton />

                <Modal.Body>
                    {/* Si está logeado muestra formulario. Sino avisa de que no puede crear ingrediente */}
                    {(isLoggedIn) ?
                        <CreateIngredient />
                        : <Alert variant="danger">
                            <FormattedMessage id="common.alerts.notLoggedIn" />
                        </Alert>
                    }
                </Modal.Body>
            </Modal>
        </>
    )
}

export type {Props as FindRecipeIngredientProps};
export default FindRecipeIngredient;
