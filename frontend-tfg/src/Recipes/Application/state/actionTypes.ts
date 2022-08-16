import {Category, Recipe} from "../../Domain";

/* ******************** Nombres de las acciones ******************** */

export const CREATE_CATEGORY: string = 'recipes/createCategory';
export const GET_CATEGORIES: string = 'recipes/findCategories';
export const CREATE_RECIPE: string = 'recipes/createRecipe';


/* ******************** Tipos de las acciones ******************** */

export interface CreateCategoryActionType {
    type: string,
    payload: Category
}

export interface GetCategoriesActionType {
    type: string,
    payload: Array<Category>
}

export interface CreateRecipeActionType {
    type: string,
    payload: Recipe
}

export type RecipeDispatchType = CreateCategoryActionType
    | GetCategoriesActionType
    | CreateRecipeActionType
;
