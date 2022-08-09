import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";
import IngredientTypesList, {IngredientTypesListProps} from "./IngredientTypesList";
import {useState} from "react";
import CreateIngredient from "./CreateIngredient";
import FindIngredients from "./FindIngredients";

const IngredientsPage = () => {
    const [selectedItemIndex, setSelectedItemIndex] = useState<number>(-1);

    const handleIngredientTypeClick = (event: MouseEvent, index: number) => {
        event.preventDefault();

        setSelectedItemIndex(index);
    }

    let ingredientTypesListProps: IngredientTypesListProps = {
        onClickCallback: handleIngredientTypeClick,
        selectedIndex: selectedItemIndex
    }


    return (
        <Row>
            {/* Formulario para crear y mostrar los tipos y los ingredientes */}
            <Col md={4}>
                <CreateIngredientType />
                <CreateIngredient />
                <IngredientTypesList {...ingredientTypesListProps}/>
            </Col>

            {/* Columna para buscar ingredientes */}
            <Col>
                <FindIngredients />
            </Col>
        </Row>
    )
};

export default IngredientsPage;
