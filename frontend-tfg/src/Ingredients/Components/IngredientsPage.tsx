import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";
import {useEffect} from "react";
import CreateIngredient from "./CreateIngredient";
import FindIngredients from "./FindIngredients";
import FindIngredientsResults from "./FindIngredientsResults";
import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {ingredientsRedux} from "../Application";
import {SearchCriteria} from "../../App";

const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const IngredientsPage = () => {
    const dispatch = useAppDispatch();
    const isAdminLoggedIn = useAppSelector(userRedux.selectors.selectIsAdmin);
    const searchCriteria = useAppSelector(ingredientsRedux.selectors.selectSearchCriteria);

    useEffect( () => {
        let criteria: SearchCriteria = {
            page: searchCriteria.page,
            pageSize: DEFAULT_PAGE_SIZE,
            type: null,
            name: null,
        }
        let onSuccess = () => {};
        dispatch(ingredientsRedux.actions.findAllIngredientsAsyncAction(criteria, onSuccess));
    }, [dispatch, searchCriteria.page])

    return (
        <Row>
            {/* Formulario para crear y mostrar los tipos y los ingredientes */}
            {(isAdminLoggedIn) &&
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
                    <FindIngredients />
                    <FindIngredientsResults />
                </Row>
            </Col>
        </Row>
    )
}

export default IngredientsPage;
