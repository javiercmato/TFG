import {Category, Recipe} from "../../Domain";


interface IRecipeState {
    categories: Array<Category>                     // Categorías de recetas existentes
    recipe: Nullable<Recipe>                        // Información de la receta actual
}


const initialState: IRecipeState = {
    categories: [],
    recipe: null,
}


export type {IRecipeState};
export {initialState};
