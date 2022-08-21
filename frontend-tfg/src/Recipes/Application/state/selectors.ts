import {RootState} from "../../../store";
import {IRecipeState} from "./IRecipeState";
import {Category, Recipe} from "../../Domain";


const getModuleState = (state: RootState): IRecipeState => state.recipes;

/* ******************** DATOS DE CATEGORIAS ******************** */

export const selectCategories = (state: RootState) : Array<Category> => getModuleState(state).categories;


/* ******************** DATOS DE RECETA ******************** */

export const selectRecipe = (state: RootState) : Nullable<Recipe> => getModuleState(state).recipe;


