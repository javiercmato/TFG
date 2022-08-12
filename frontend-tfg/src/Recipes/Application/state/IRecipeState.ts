import {Category} from "../../Domain";


interface IRecipeState {
    categories: Array<Category>                     // Categorías de recetas existentes
}


const initialState: IRecipeState = {
    categories: [],
}


export type {IRecipeState};
export {initialState};
