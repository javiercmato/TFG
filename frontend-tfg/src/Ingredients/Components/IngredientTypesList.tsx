import {Alert, ListGroup} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import IngredientTypesListItem from "./IngredientTypesListItem";
import {useSelector} from "react-redux";
import {ingredientsRedux} from "../Application";
import {listTitle} from "./styles/ingredientTypesList";

interface Props {
    onClickCallback: any,
    selectedIndex: number,
}

const IngredientTypesList = ({onClickCallback, selectedIndex}: Props) => {
    const ingredientTypes = useSelector(ingredientsRedux.selectors.selectIngrediendtTypes);


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
            <h4 style={listTitle}>
                <FormattedMessage id="ingredients.components.IngredientTypesList.quickFilterByTypes" />
            </h4>

            <ListGroup>
                {ingredientTypes.map( (i, index: number) => {
                    let isActive = (selectedIndex === index);
                    return (
                        <IngredientTypesListItem
                            key={i.id}
                            item={i}
                            isActive={isActive}
                            index={index}
                            onClickCallback={onClickCallback}
                        />
                    )}
                )}
            </ListGroup>
        </div>
    )
};

export type {Props as IngredientTypesListProps};
export default IngredientTypesList;
