import {IngredientType} from "../Domain";
import {Alert, ListGroup} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import IngredientTypesListItem from "./IngredientTypesListItem";

interface Props {
    list: Array<IngredientType>,
    onClickCallback: any,
    selectedIndex: number,
}

const IngredientTypesList = ({list, onClickCallback, selectedIndex}: Props) => {

    // Si no hay resultados, no se muestra nada
    if (!list) return null;

    // Si no hay ninguna coincidencia, se muestra una alerta
    if (list.length === 0) {
        return (
            <Alert variant="info">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    return (
        <div>
            <ListGroup>
                {list.map( (i, index: number) => {
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
