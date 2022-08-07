import {RootState} from "../../../store";
import {IIngredientState} from "./IIngredientState";
import {IngredientType} from "../../Domain";


const getModuleState = (state: RootState) : IIngredientState => state.ingredients;


/* ******************** DATOS DE TIPOS DE INGREDIENTE ******************** */
export const selectIngrediendtTypes = (state: RootState) : Array<IngredientType> => getModuleState(state).types;


export const getIngredientTypeName = (types: Array<IngredientType>, id: string) => {
    if (!types) return '';

    const ingredientType = types.find((t) => t.id === id);
    if (!ingredientType) return '';

    return ingredientType.name;
}
