import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";
import IngredientTypesList, {IngredientTypesListProps} from "./IngredientTypesList";
import {useState} from "react";
import {IngredientType} from "../Domain";
import {useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";

const IngredientsPage = () => {
    const ingredientTypes: IngredientType[] = useAppSelector(ingredientsRedux.selectors.selectIngrediendtTypes);
    const [selectedItemIndex, setSelectedItemIndex] = useState<number>(-1);

    const handleIngredientTypeClick = (event: MouseEvent, index: number) => {
        event.preventDefault();

        setSelectedItemIndex(index);
    }

    let props: IngredientTypesListProps = {
        list: ingredientTypes,
        onClickCallback: handleIngredientTypeClick,
        selectedIndex: selectedItemIndex
    }

    return (
        <Row>
            {/* Formulario para crear y mostrar los tipos de ingredientes */}
            <Col md={4}>
                <CreateIngredientType />
                <IngredientTypesList {...props}/>
            </Col>

            {/* Columna para buscar ingredientes */}
            <Col>
                <div>
                {(selectedItemIndex) >= 0 ?
                    ingredientTypes.at(selectedItemIndex)!.name
                    : <h2>NINGÚN TIPO SELECCIONADO</h2>
                }
                </div>
            </Col>
        </Row>
    )
};

export default IngredientsPage;
