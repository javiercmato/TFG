import {UserSummaryDTO} from "../Infrastructure";
import {RecipeSummaryDTO} from "../../Recipes";

interface PrivateList {
    id: string,
    title: string,
    creator: UserSummaryDTO,
    recipes: Array<RecipeSummaryDTO>
}

export default PrivateList;
