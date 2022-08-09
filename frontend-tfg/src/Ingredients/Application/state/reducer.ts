import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {
    CreateIngredientTypeActionType,
    FindAllIngredientsActionType,
    FindAllIngredientTypesActionType,
    FindIngredientsByNameActionType,
    FindIngredientsByNameAndTypeActionType,
    FindIngredientsByTypeActionType,
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


const ingredientSearch = (state: Nullable<Search<Ingredient>> = initialState.ingredientSearch,
                          action: IngredientDispatchType): Nullable<Search<Ingredient>> => {
    switch (action.type) {
        case actionTypes.FIND_ALL_INGREDIENTS : {
            let search: Search<Ingredient> = (action as FindAllIngredientsActionType).payload;

            return search;
        }

        case actionTypes.FIND_INGREDIENTS_BY_TYPE : {
            let search: Search<Ingredient> = (action as FindIngredientsByTypeActionType).payload;

            return search;
        }

        case actionTypes.FIND_INGREDIENTS_BY_NAME : {
            let search: Search<Ingredient> = (action as FindIngredientsByNameActionType).payload;

            return search;
        }

        case actionTypes.FIND_INGREDIENTS_BY_NAME_AND_TYPE : {
            let search: Search<Ingredient> = (action as FindIngredientsByNameAndTypeActionType).payload;

            return search;
        }

        default:
            return state;
    }
}


const ingredientsReducer = combineReducers<IIngredientState>({
    types: ingredientTypes,
    ingredientSearch: ingredientSearch,
});


export default ingredientsReducer;
