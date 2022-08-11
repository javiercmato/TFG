import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {
    CreateIngredientTypeActionType,
    FindAllIngredientsActionType,
    FindAllIngredientTypesActionType,
    FindIngredientsActionType,
    IngredientDispatchType
} from './actionTypes';
import {IIngredientState, initialState} from "./IIngredientState";
import {Ingredient, IngredientType} from "../../Domain";
import {Search} from "../../../App";


const ingredientTypes = (state: Array<IngredientType> = initialState.types,
                         action: IngredientDispatchType): Array<IngredientType> => {
    switch (action.type) {
        case actionTypes.FIND_ALL_INGREDIENT_TYPES:
            return (action as FindAllIngredientTypesActionType).payload;

        case actionTypes.CREATE_INGREDIENT_TYPE:
            let payload: IngredientType = (action as CreateIngredientTypeActionType).payload;

            return [...state, payload]

        default:
            return state;
    }
}


const ingredientSearch = (state: Search<Ingredient> = initialState.ingredientSearch,
                          action: IngredientDispatchType): Search<Ingredient> => {
    switch (action.type) {
        case actionTypes.FIND_ALL_INGREDIENTS : {
            let search: Search<Ingredient> = (action as FindAllIngredientsActionType).payload;

            return search;
        }

        case actionTypes.FIND_INGREDIENTS : {
            let search: Search<Ingredient> = (action as FindIngredientsActionType).payload;

            return search;
        }

        case actionTypes.CLEAR_INGREDIENTS_SEARCH:
            return initialState.ingredientSearch;

        default:
            return state;
    }
}


const ingredientsReducer = combineReducers<IIngredientState>({
    types: ingredientTypes,
    ingredientSearch: ingredientSearch,
});


export default ingredientsReducer;
