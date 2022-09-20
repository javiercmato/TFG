import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";
import {useEffect} from "react";
import CreateIngredient from "./CreateIngredient";
import FindIngredients from "./FindIngredients";
import FindIngredientsResults from "./FindIngredientsResults";
import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {ingredientsRedux} from "../Application";
import {defaultSearchCriteria, SearchCriteria} from "../../App";
import {FormattedMessage} from "react-intl";

const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const IngredientsPage = () => {
    const dispatch = useAppDispatch();
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);
    const searchCriteria = useAppSelector(ingredientsRedux.selectors.selectSearchCriteria);

    useEffect( () => {
        let criteria: SearchCriteria = {
            ...defaultSearchCriteria,
            page: searchCriteria.page,
            pageSize: DEFAULT_PAGE_SIZE,
        }
        let onSuccess = () => {};
        dispatch(ingredientsRedux.actions.findAllIngredientsAsyncAction(criteria, onSuccess));

        // Limpiar del store los ingredientes al cambiar de página
        return () => {
            dispatch(ingredientsRedux.actions.clearIngredientsSearchAction());
        }
    }, [dispatch, searchCriteria.page])

    return (
        <Row>
            {/* Formulario para crear y mostrar los tipos y los ingredientes */}
            {(isLoggedIn) &&
                <Col md={4} >
                    <Row className={"gy-3"}>
                        <CreateIngredientType />
                        <CreateIngredient />
                    </Row>
                </Col>
            }

            {/* Columna para buscar ingredientes */}
            <Col>
                <Row className={"gy-3"}>
                    <h4><FormattedMessage id="ingredients.components.FindIngredients.findIngredients" /></h4>
                    <FindIngredients />
                    <FindIngredientsResults />
                </Row>
            </Col>
        </Row>
    )
}

export default IngredientsPage;
