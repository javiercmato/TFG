import {Alert, Button, Table} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {FaTrash} from "react-icons/fa";
import {useEffect} from "react";


/** Datos a mostrar de los ingredientes añadidos a una receta */
interface DisplayCustomIngredient {
    ingredientID: string,
    ingredientName: string,
    quantity: string,
    measureUnit: string
}

interface Props {
    items: Array<DisplayCustomIngredient>,
    onRemoveItemCallback: (ingredientID: string) => void,
}

const FinalRecipeIngredientsList = ({items, onRemoveItemCallback}: Props) => {

    const handleRemoveClick = (e: any, item: DisplayCustomIngredient) => {
        e.preventDefault();

        onRemoveItemCallback(item.ingredientID);
    }

    // Actualizar lista cada vez que cambien los elementos
    useEffect( () => {}, [items]);


    if (items.length === 0) {
        return (
            <Alert variant="info">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    return (
        <Table bordered hover>
            <thead>
                <tr>
                    <th>
                        <FormattedMessage id="common.fields.ingredient" />
                    </th>
                    <th>
                        <FormattedMessage id="common.fields.quantity" />
                    </th>
                    <th>
                        <FormattedMessage id="common.fields.measureUnit" />
                    </th>
                    <th>
                        <FormattedMessage id="common.buttons.remove" />
                    </th>
                </tr>
            </thead>

            <tbody>
                {items.map((item: DisplayCustomIngredient, index: number) =>
                    <tr key={index}>
                        <td>
                            {item.ingredientName}
                        </td>
                        <td>
                            {item.quantity}
                        </td>
                        <td>
                            {item.measureUnit}
                        </td>
                        <td>
                            <Button variant="danger" onClick={(e) => handleRemoveClick(e, item)}>
                                <FaTrash />
                            </Button>
                        </td>
                    </tr>
                )}
            </tbody>
        </Table>
    )
}

export type {DisplayCustomIngredient};
export type {Props as FinalRecipeIngredientsListProps};
export default FinalRecipeIngredientsList;
