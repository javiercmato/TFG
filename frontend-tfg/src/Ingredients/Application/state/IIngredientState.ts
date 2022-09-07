import {Ingredient, IngredientType} from "../../Domain";
import {Search} from "../../../App";
import defaultSearch from "../../../App/Domain/common/Search";


/** Tipo de estado para información asociada a los ingredientes */
interface IIngredientState {
    types: Array<IngredientType>                    // Tipos de ingredientes existentes,
    measures: Array<string>                         // Unidades de medida de ingredientes en recetas
    ingredientSearch: Search<Ingredient>,           // Búsqueda de ingredientes y resultados obtenidos
}

const initialState: IIngredientState = {
    types: [],
    measures: [],
    ingredientSearch: defaultSearch,
}

export {initialState};
export type {IIngredientState};
