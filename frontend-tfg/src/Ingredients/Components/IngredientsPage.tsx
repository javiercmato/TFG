import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";
import IngredientTypesList, {IngredientTypesListProps} from "./IngredientTypesList";
import {useEffect, useState} from "react";
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
    const [selectedItemIndex, setSelectedItemIndex] = useState<number>(-1);
    const searchCriteria = useAppSelector(ingredientsRedux.selectors.selectSearchCriteria);
    const [currentPage, setCurrentPage] = useState<number>(searchCriteria.page);

    const handleIngredientTypeClick = (event: any, index: number, typeID: string) => {
        event.preventDefault();

        setSelectedItemIndex(index);
        let criteria: SearchCriteria = {
            page: currentPage,
            type: typeID,
            pageSize: DEFAULT_PAGE_SIZE
        }
        let onSuccess = () => {};
        console.log(event.target.value);
        dispatch(ingredientsRedux.actions.findIngredientsByTypeAsyncAction(criteria, onSuccess));
    }

    const handlePreviousResultsPage = (event: any) => {
        event.preventDefault();

        setCurrentPage((prevPage) => prevPage - 1);
    }

    const handleNextResultsPage = (event: any) => {
        event.preventDefault();

        setCurrentPage((prevPage) => prevPage + 1);
    }


    let ingredientTypesListProps: IngredientTypesListProps = {
        onClickCallback: handleIngredientTypeClick,
        selectedIndex: selectedItemIndex
    }

    useEffect( () => {
        let criteria: SearchCriteria = {
            page: currentPage,
            pageSize: DEFAULT_PAGE_SIZE
        }
        let onSuccess = () => {};
        dispatch(ingredientsRedux.actions.findAllIngredientsAsyncAction(criteria, onSuccess));
    }, [searchCriteria.page])

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
                    <IngredientTypesList {...ingredientTypesListProps}/>
                </Row>
            </Col>
        </Row>
    )
};

export default IngredientsPage;
