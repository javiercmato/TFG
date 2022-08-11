import {RootState} from "../../../store";
import {IIngredientState} from "./IIngredientState";
import {Ingredient, IngredientType} from "../../Domain";
import {Block, Search, SearchCriteria} from "../../../App";


const getModuleState = (state: RootState) : IIngredientState => state.ingredients;

/* ******************** DATOS DE TIPOS DE INGREDIENTE ******************** */

export const selectIngrediendtTypes = (state: RootState) : Array<IngredientType> => getModuleState(state).types;


export const getIngredientTypeName = (types: Array<IngredientType>, id: string) => {
    if (!types) return '';

    const ingredientType = types.find((t) => t.id === id);
    if (!ingredientType) return '';

    return ingredientType.name;
}


/* ******************** DATOS DE BÚSQUEDAS DE INGREDIENTES ******************** */

export const getIngredientSearch = (state: RootState) : Search<Ingredient> => getModuleState(state).ingredientSearch;

export const selectSearchCriteria = (state: RootState) : SearchCriteria => getIngredientSearch(state).criteria;

export const selectSearchResultBlock = (state: RootState) : Nullable<Block<Ingredient>> => getIngredientSearch(state).result;
