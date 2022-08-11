import {User} from "../../Users";
import IngredientType from "./IngredientType";

interface Ingredient {
    id: string,
    name: string,
    ingredientType: IngredientType,
    creator: User
}

export default Ingredient;
