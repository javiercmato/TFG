import {useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";
import {IngredientType} from "../Domain";
import {Alert, ListGroup} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import IngredientTypesListItem from "./IngredientTypesListItem";
import {useState} from "react";

const IngredientTypesList = () => {
    const ingredientTypes: IngredientType[] = useAppSelector(ingredientsRedux.selectors.selectIngrediendtTypes);
    const [selectedItemIndex, setSelectedItemIndex] = useState<number>();

    const handleClick = (event: MouseEvent, index: number) => {
        event.preventDefault();


        setSelectedItemIndex(index);
    }

    // Si no hay resultados, no se muestra nada
    if (!ingredientTypes) return null;

    // Si no hay ninguna coincidencia, se muestra una alerta
    if (ingredientTypes.length === 0) {
        return (
            <Alert variant="info">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    return (
        <div>
            <ListGroup>
                {ingredientTypes.map( (i, index: number) => {
                    let isActive = (selectedItemIndex === index);
                    return (
                        <IngredientTypesListItem
                            key={i.id}
                            item={i}
                            isActive={isActive}
                            index={index}
                            onClickCallback={handleClick}
                        />
                    )}
                )}
            </ListGroup>
        </div>
    )
};

export default IngredientTypesList;
