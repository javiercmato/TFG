import {RecipeSummaryDTO} from "../../Recipes";

interface PrivateList {
    id: string,
    title: string,
    description: string,
    recipes: Array<RecipeSummaryDTO>
}

export default PrivateList;
