import {Category, Recipe} from "../../Domain";
import {Search} from "../../../App";

/* ******************** Nombres de las acciones ******************** */

export const CREATE_CATEGORY: string = 'recipes/createCategory';
export const GET_CATEGORIES: string = 'recipes/findCategories';
export const CREATE_RECIPE: string = 'recipes/createRecipe';
export const GET_RECIPE_DETAILS: string = 'recipes/getRecipeDetails';
export const FIND_RECIPES: string = 'recipes/findRecipes';
export const CLEAR_RECIPES_SEARCH: string = 'recipes/clearRecipesSearch';

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

export interface GetRecipeDetailsActionType {
    type: string,
    payload: Recipe
}

export interface FindRecipesActionType {
    type: string,
    payload: Search<Recipe>
}

export interface ClearRecipesSearchActionType {
    type: string,
}

export type RecipeDispatchType = CreateCategoryActionType
    | GetCategoriesActionType
    | CreateRecipeActionType
    | GetRecipeDetailsActionType
    | FindRecipesActionType
    | ClearRecipesSearchActionType
;
