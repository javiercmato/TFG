import {RootState} from "../../../store";
import {IRecipeState} from "./IRecipeState";
import {Category} from "../../Domain";


const getModuleState = (state: RootState): IRecipeState => state.recipes;

/* ******************** DATOS DE CATEGORIAS ******************** */

export const selectCategories = (state: RootState) : Array<Category> => getModuleState(state).categories;


