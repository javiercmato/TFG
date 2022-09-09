import RecipeIngredient from "./RecipeIngredient";
import RecipePicture from "./RecipePicture";
import RecipeStep from "./RecipeStep";
import {Comment} from "../../Social";

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
    pictures: Array<RecipePicture>,
    steps: Array<RecipeStep>,
    comments: Array<Comment>,
    totalVotes: number,
    averageRating: number,
    version: number
}


export default Recipe;
