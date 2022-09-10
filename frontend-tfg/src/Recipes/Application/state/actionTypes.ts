import {Category, Recipe} from "../../Domain";
import {Search} from "../../../App";
import {RecipeSummaryDTO} from "../../Infrastructure";
import {Comment} from "../../../Social";

/* ******************** Nombres de las acciones ******************** */

export const CREATE_CATEGORY: string = 'recipes/createCategory';
export const GET_CATEGORIES: string = 'recipes/findCategories';
export const CREATE_RECIPE: string = 'recipes/createRecipe';
export const GET_RECIPE_DETAILS: string = 'recipes/getRecipeDetails';
export const CLEAR_RECIPE_DETAILS: string = 'recipes/clearRecipeDetails';
export const FIND_RECIPES: string = 'recipes/findRecipes';
export const CLEAR_RECIPES_SEARCH: string = 'recipes/clearRecipesSearch';
export const DELETE_RECIPE: string = 'recipes/deleteRecipe';
export const BAN_RECIPE: string = 'recipes/banRecipe';
export const GET_RECIPE_COMMENTS: string = "social/getRecipeComments";
export const ADD_COMMENT: string = "social/addComment";
export const BAN_COMMENT: string = "social/banComment";

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

export interface ClearRecipeDetailsActionType {
    type: string,
}

export interface FindRecipesActionType {
    type: string,
    payload: Search<RecipeSummaryDTO>
}

export interface ClearRecipesSearchActionType {
    type: string,
}

export interface DeleteRecipeActionType {
    type: string
}

export interface BanRecipeActionType {
    type: string,
    payload: boolean
}

export interface GetRecipeCommentsActionType {
    type: string,
    payload: Search<Comment>
}

export interface AddCommentActionType {
    type: string,
    payload: Comment
}

export interface BanCommentActionType {
    type: string,
    payload: Comment
}



export type RecipeDispatchType = CreateCategoryActionType
    | GetCategoriesActionType
    | CreateRecipeActionType
    | GetRecipeDetailsActionType
    | ClearRecipeDetailsActionType
    | FindRecipesActionType
    | ClearRecipesSearchActionType
    | DeleteRecipeActionType
    | BanRecipeActionType
    | GetRecipeCommentsActionType
    | AddCommentActionType
    | BanCommentActionType
;
