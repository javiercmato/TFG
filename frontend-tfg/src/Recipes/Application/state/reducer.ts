import {combineReducers} from "redux";
import * as actionTypes from './actionTypes';
import {
    CreateCategoryActionType,
    GetCategoriesActionType,
    GetRecipeDetailsActionType,
    RecipeDispatchType
} from './actionTypes';
import {initialState, IRecipeState} from "./IRecipeState";
import {Category, Recipe} from "../../Domain";


const categories = (state: Array<Category> = initialState.categories,
                    action: RecipeDispatchType): Array<Category> => {
    switch (action.type) {
        case actionTypes.CREATE_CATEGORY: {
            let payload: Category = (action as CreateCategoryActionType).payload;

            return [...state, payload];
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

        default:
            return state;

    }
}


const recipesReducer = combineReducers<IRecipeState>({
    categories: categories,
    recipe: recipes,
});

export default recipesReducer;
