/** Tipo de estado para información asociada a los ingredientes */
import {Ingredient, IngredientType} from "../../Domain";
import {Search} from "../../../App";


interface IIngredientState {
    types: Array<IngredientType>,                               // Tipos de ingredientes existentes,
    ingredientSearch: Nullable<Search<Ingredient>>,             // Búsqueda de ingredientes y resultados obtenidos
}

const initialState: IIngredientState = {
    types: [],
    ingredientSearch: null,
}

export {initialState};
export type {IIngredientState};
