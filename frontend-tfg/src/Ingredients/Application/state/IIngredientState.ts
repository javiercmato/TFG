/** Tipo de estado para información asociada a los ingredientes */
import {Ingredient, IngredientType} from "../../Domain";
import {Search} from "../../../App";
import defaultSearch from "../../../App/Domain/common/Search";


interface IIngredientState {
    types: Array<IngredientType>                    // Tipos de ingredientes existentes,
    ingredientSearch: Search<Ingredient>,           // Búsqueda de ingredientes y resultados obtenidos
}

const initialState: IIngredientState = {
    types: [],
    ingredientSearch: defaultSearch,
}

export {initialState};
export type {IIngredientState};
