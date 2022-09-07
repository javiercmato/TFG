import RecipeIngredient from "./RecipeIngredient";
import RecipePicture from "./RecipePicture";
import RecipeStep from "./RecipeStep";

interface Recipe {
    id: string,
    name: string,
    description: Nullable<string>,
    creationDate: Date,
    duration: number,
    diners: Nullable<number>,
    isBannedByAdmin: boolean,
    authorID: string,
    categoryID: string,
    ingredients: Array<RecipeIngredient>,
    pictures: Nullable<Array<RecipePicture>>,
    steps: Array<RecipeStep>
}


export default Recipe;
