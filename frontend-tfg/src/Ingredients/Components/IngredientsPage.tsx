import {Col, Row} from "react-bootstrap";
import CreateIngredientType, {CreateIngredientTypeProps} from "./CreateIngredientType";
import {useEffect, useState} from "react";
import CreateIngredient, {CreateIngredientProps} from "./CreateIngredient";
import FindIngredients from "./FindIngredients";
import FindIngredientsResults from "./FindIngredientsResults";
import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {ingredientsRedux} from "../Application";
import {defaultSearchCriteria, ErrorDto, Errors, SearchCriteria} from "../../App";
import {FormattedMessage} from "react-intl";

const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const IngredientsPage = () => {
    const dispatch = useAppDispatch();
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);
    const searchCriteria = useAppSelector(ingredientsRedux.selectors.selectSearchCriteria);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);


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


    let createTypeProps: CreateIngredientTypeProps = {
        onBackendError: (error: ErrorDto) => setBackendErrors(error),
    }
    let createIngredientProps: CreateIngredientProps = {
        onBackendError: (error: ErrorDto) => setBackendErrors(error),
    }

    return (
        <>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            <Row>
                {/* Formulario para crear y mostrar los tipos y los ingredientes */}
                {(isLoggedIn) &&
                    <Col md={4} >
                        <Row className={"gy-3"}>
                            <CreateIngredientType {...createTypeProps}/>
                            <CreateIngredient {...createIngredientProps} />
                        </Row>
                    </Col>
                }

                {/* Columna para buscar ingredientes */}
                <Col className={"mx-4"}>
                    <Row className={"gy-3"}>
                        <h4><FormattedMessage id="ingredients.components.FindIngredients.findIngredients" /></h4>
                        <FindIngredients />
                        <FindIngredientsResults />
                    </Row>
                </Col>
            </Row>
        </>
    )
}

export default IngredientsPage;
