import FindRecipeIngredient from "./FindRecipeIngredient";
import AddRecipeIngredient, {AddRecipeIngredientProps} from "./AddRecipeIngredient";
import {CreateRecipeIngredientParamsDTO} from "../Infrastructure";
import {Col, Container, Row} from "react-bootstrap";

interface Props {

}

const RecipeIngredientsForm = ({}: Props) => {
    // Ingredientes que el usuario ha añadido a la receta
    const addedIngredients: CreateRecipeIngredientParamsDTO[] = [];


    const addIngredientCallback = (recipeIngredient: CreateRecipeIngredientParamsDTO) => {
        addedIngredients.push(recipeIngredient);
    }

    let addRecipeIngredientProps: AddRecipeIngredientProps = {
        onAddIngredientCallback: addIngredientCallback
    }

    return (
        <Container>
            <Row>
                <Col>
                    <FindRecipeIngredient />
                    <AddRecipeIngredient {...addRecipeIngredientProps} />
                </Col>

                <Col>

                </Col>
            </Row>
        </Container>
    )
}


export type {Props as RecipeIngredientsFormProps};
export default RecipeIngredientsForm;
