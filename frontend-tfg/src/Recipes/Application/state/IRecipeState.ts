import {Category, Recipe} from "../../Domain";
import {Search} from "../../../App";
import defaultSearch from "../../../App/Domain/common/Search";
import RecipeSummaryDTO from "../../Infrastructure/RecipeSummaryDTO";


interface IRecipeState {
    categories: Array<Category>                     // Categorías de recetas existentes
    recipe: Nullable<Recipe>                        // Información de la receta actual
    recipeSearch: Search<RecipeSummaryDTO>          // Búsqueda de recetas y resultados obtenidos
}


const initialState: IRecipeState = {
    categories: [],
    recipe: null,
    recipeSearch: defaultSearch
}


export type {IRecipeState};
export {initialState};
