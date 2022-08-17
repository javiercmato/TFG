import {useAppSelector} from "../../store";
import {Ingredient, ingredientsRedux} from "../../Ingredients";
import {Block} from "../../App";
import {Alert, Button, Table} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {tableContent} from "./styles/addRecipeIngredient";
import AddIngredientToRecipeModal, {AddIngredientToRecipeModalProps} from "./AddIngredientToRecipeModal";
import {useState} from "react";

interface Props {
    onAddIngredientCallback: any
}

const AddRecipeIngredient = ({onAddIngredientCallback}: Props) => {
    const ingredientsBlock : Block<Ingredient> = useAppSelector(ingredientsRedux.selectors.selectSearchResultBlock)!;
    const [showModal, setShowModal] = useState<boolean>(false);
    const [selectedIngredient, setSelectedIngredient] = useState<Ingredient>();

    let addIngredientProps: AddIngredientToRecipeModalProps = {
        show: showModal,
        ingredient: selectedIngredient,
        onHideCallback: () => setShowModal(false),
        onAddIngredientCallback: onAddIngredientCallback
    }


    const handleAddClick = (ingredient: Ingredient): any => {
        // Indica que se ha cambiado el ingrediente seleccionado
        setSelectedIngredient(ingredient);

        // Enseña formulario para añadir ingrediente a a la receta
        setShowModal(true);
    }




    if (ingredientsBlock === null || ingredientsBlock.itemsCount === 0) {
        return (
            <Alert variant="info">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    return (
        <>
            <Table bordered hover
                   style={tableContent}
            >
                <thead>
                    <tr>
                        <th>
                            <FormattedMessage id="common.fields.ingredients" />
                        </th>
                        <th>
                            <FormattedMessage id="common.buttons.add" />
                        </th>
                    </tr>
                </thead>

                <tbody>
                    {ingredientsBlock.items.map((item) =>
                        <tr key={item.id}>
                            {/* Nombre del ingrediente */}
                            <td>
                                {item.name}
                            </td>

                            {/* Botón para añadir ingrediente seleccionado */}
                            <td>
                                <Button onClick={() => handleAddClick(item)}>
                                    <FormattedMessage id="common.buttons.add" />
                                </Button>
                            </td>
                        </tr>
                    )}
                </tbody>
            </Table>

            <AddIngredientToRecipeModal {...addIngredientProps} />
        </>
    )
}


export type {Props as AddRecipeIngredientProps};
export default AddRecipeIngredient;
