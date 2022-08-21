import {RootState} from "../../../store";
import {IRecipeState} from "./IRecipeState";
import {Category, Recipe} from "../../Domain";


const getModuleState = (state: RootState): IRecipeState => state.recipes;

/* ******************** DATOS DE CATEGORIAS ******************** */

export const selectCategories = (state: RootState) : Array<Category> => getModuleState(state).categories;

/** Devuelve el nombre de la categoría a partir de las categorías cargadas en memoria */
export const selectCategoryName = (categories: Array<Category>, categoryId: string) : string => {
    if (!categories) return '';

    const category = categories.find((c: Category) => c.id === categoryId);
    if (!category) return '';

    return category.name;
}

/* ******************** DATOS DE RECETA ******************** */

export const selectRecipe = (state: RootState) : Nullable<Recipe> => getModuleState(state).recipe;


