import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";
import IngredientTypesList, {IngredientTypesListProps} from "./IngredientTypesList";
import {useState} from "react";
import {IngredientType} from "../Domain";
import {useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";
import CreateIngredient, {CreateIngredientProps} from "./CreateIngredient";

const IngredientsPage = () => {
    const ingredientTypes: IngredientType[] = useAppSelector(ingredientsRedux.selectors.selectIngrediendtTypes);
    const [selectedItemIndex, setSelectedItemIndex] = useState<number>(-1);

    const handleIngredientTypeClick = (event: MouseEvent, index: number) => {
        event.preventDefault();

        setSelectedItemIndex(index);
    }

    let ingredientTypesListProps: IngredientTypesListProps = {
        list: ingredientTypes,
        onClickCallback: handleIngredientTypeClick,
        selectedIndex: selectedItemIndex
    }

    let createIngredientProps: CreateIngredientProps = {
        ingredientTypes: ingredientTypes
    }

    return (
        <Row>
            {/* Formulario para crear y mostrar los tipos y los ingredientes */}
            <Col md={4}>
                <CreateIngredientType />
                <CreateIngredient {...createIngredientProps} />
                <IngredientTypesList {...ingredientTypesListProps}/>
            </Col>

            {/* Columna para buscar ingredientes */}
            <Col>
            </Col>
        </Row>
    )
};

export default IngredientsPage;
