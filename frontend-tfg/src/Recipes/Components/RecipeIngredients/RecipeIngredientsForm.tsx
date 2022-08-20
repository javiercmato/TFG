import FindRecipeIngredient from "./FindRecipeIngredient";
import AddRecipeIngredient, {AddRecipeIngredientProps} from "./AddRecipeIngredient";
import {Col, Container, Row} from "react-bootstrap";
import FinalRecipeIngredientsList, {
    DisplayCustomIngredient,
    FinalRecipeIngredientsListProps
} from "./FinalRecipeIngredientsList";
import {CreateRecipeIngredientParamsDTO} from "../../Infrastructure";
import {useState} from "react";
import {FormattedMessage} from "react-intl";

/** Datos que se mostrarán en la lista de ingredientes añadidos a la receta */
interface RecipeIngredientDisplayData {
    ingredientID: string,
    quantity: string,
    measureUnit: string,
    name: string
}

interface Props {
    ingredientParams: Array<CreateRecipeIngredientParamsDTO>,
    onAddIngredientParams: (ingredientParams: Array<CreateRecipeIngredientParamsDTO>) => void,
    onRemoveIngredientParams: (ingredientID: string) => void,
}

const RecipeIngredientsForm = ({ingredientParams, onAddIngredientParams, onRemoveIngredientParams}: Props) => {
    const [recipeIngredientParams, setRecipeIngredientParams] = useState<Array<CreateRecipeIngredientParamsDTO>>(ingredientParams);           // Parámetros de los ingredientes a añadir a la receta
    const [displayableItems, setDisplayableItems] = useState<Array<DisplayCustomIngredient>>([]);                               // Información de ingredientes a visualizar en la lista

    /** Añade el ingrediente recibido a la lista de ingredientes para mostrar, junto a su cantidad y medida.
     * También actualiza los ingredientes a mostrar en la lista de resultados.
     */
    const onAddCustomizedIngredient = (ingredientName: string, params: CreateRecipeIngredientParamsDTO) => {
        // 1) Añadir los parámetros del ingrediente a la lista de parámetros
        let item: DisplayCustomIngredient = {
            ingredientID: params.ingredientID,
            quantity: params.quantity,
            measureUnit: params.measureUnit,
            ingredientName: ingredientName,
        }
        setDisplayableItems( [...displayableItems, item]);


        // 2) Crear y añadir los datos del ingrediente añadido para poder visualizarlo
        setRecipeIngredientParams( [...recipeIngredientParams, params]);
        onAddIngredientParams([...recipeIngredientParams, params]);                      // Devuelve lista de parámetros al padre

    }

    const onRemoveCustomizedIngredient = (ingredientID: string) => {
        // Eliminar el ingrediente de la lista de ingredientes añadidos
        setDisplayableItems((prevState) => prevState.filter(
            (item) => item.ingredientID !== ingredientID)
        );

        // Ejecutar el callback para borrar
        onRemoveIngredientParams(ingredientID);
    }


    let addRecipeIngredientProps: AddRecipeIngredientProps = {
        onAddCustomizedIngredientCallback: onAddCustomizedIngredient
    }

    let finalRecipeIngredientsListProps: FinalRecipeIngredientsListProps = {
        items: displayableItems,
        onRemoveItemCallback: onRemoveCustomizedIngredient,
    }

    return (
        <Container>
            <Row>
                {/* Componentes para mostrar y seleccionar ingredientes */}
                <Col>
                    <FindRecipeIngredient />
                    <AddRecipeIngredient {...addRecipeIngredientProps} />
                </Col>

                {/* Visualizar ingredientes a añadir a la receta*/}
                <Col>
                    <h4>
                        <FormattedMessage id="common.results" />
                    </h4>
                    <FinalRecipeIngredientsList {...finalRecipeIngredientsListProps} />
                </Col>
            </Row>
        </Container>
    )
}

export type {RecipeIngredientDisplayData};
export type {Props as RecipeIngredientsFormProps};
export default RecipeIngredientsForm;
