/** Tipo de estado para información asociada a los ingredientes */
import {IngredientType} from "../../Domain";


interface IIngredientState {
    types: Array<IngredientType>,                               // Tipos de ingredientes existentes,
    //ingredientSearch: Nullable<Search<Ingredient>>,           // Búsqueda de ingredientes y resultados obtenidos
}

const initialState: IIngredientState = {
    types: [],
    //ingredientSearch: null,
}

export {initialState};
export type {IIngredientState};
