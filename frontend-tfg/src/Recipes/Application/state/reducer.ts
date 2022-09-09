import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';
import {
    AddCommentActionType,
    BanRecipeActionType,
    FindRecipesActionType,
    GetCategoriesActionType,
    GetRecipeCommentsActionType,
    GetRecipeDetailsActionType,
    RecipeDispatchType
} from './actionTypes';
import {initialState, IRecipeState} from "./IRecipeState";
import {Category, Recipe} from "../../Domain";
import {Search} from "../../../App";
import RecipeSummaryDTO from "../../Infrastructure/RecipeSummaryDTO";
import {Comment} from "../../../Social";


const categories = (state: Array<Category> = initialState.categories,
                    action: RecipeDispatchType): Array<Category> => {
    switch (action.type) {
        case actionTypes.CREATE_CATEGORY: {
            //let payload: Category = (action as CreateCategoryActionType).payload;

            return state;
        }

        case actionTypes.GET_CATEGORIES: {
            let payload: Array<Category> = (action as GetCategoriesActionType).payload;

            return payload;
        }

        default:
            return state;
    }
}

const recipes = (state: Nullable<Recipe> = initialState.recipe,
                 action: RecipeDispatchType) : Nullable<Recipe> => {
    switch (action.type) {
        case actionTypes.GET_RECIPE_DETAILS: {
            let payload: Recipe = (action as GetRecipeDetailsActionType).payload;

            return payload;
        }

        case actionTypes.ADD_COMMENT: {
            // Si no hay receta cargada, se devuelve el estado (nulo)
            if (state === null) return state;

            let payload: Comment = (action as AddCommentActionType).payload;
            let commentsList = (state.comments) ? [...state.comments, payload] : [payload]

            return ({...state,
                comments: commentsList,
            })
        }

        case actionTypes.GET_RECIPE_COMMENTS: {
            // Si no hay receta cargada, se devuelve el estado (nulo)
            if (state === null) return state;

            let payload: Search<Comment> = (action as GetRecipeCommentsActionType).payload;
            let items: Nullable<Array<Comment>> = payload?.result?.items ?? null;

            return ({...state, comments: items});
        }

        case actionTypes.CLEAR_RECIPE_DETAILS:
            return initialState.recipe;

        case actionTypes.DELETE_RECIPE:
            return initialState.recipe;

        case actionTypes.BAN_RECIPE: {
            let payload: boolean = (action as BanRecipeActionType).payload;
            return ({...state, isBannedByAdmin: payload}) as Nullable<Recipe>;
        }

        default:
            return state;
    }
}

const recipesSearch = (state: Search<RecipeSummaryDTO> = initialState.recipeSearch,
                       action: RecipeDispatchType): Search<RecipeSummaryDTO> => {
    switch (action.type) {
        case actionTypes.FIND_RECIPES: {
            let search: Search<RecipeSummaryDTO> = (action as FindRecipesActionType).payload;

            return search;
        }

        case actionTypes.CLEAR_RECIPES_SEARCH:
            return initialState.recipeSearch;

        default:
            return state;
    }
}

const recipesReducer = combineReducers<IRecipeState>({
    categories: categories,
    recipe: recipes,
    recipeSearch: recipesSearch
});

export default recipesReducer;
