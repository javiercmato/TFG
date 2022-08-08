import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {CreateIngredientTypeActionType, FindAllIngredientTypesActionType, IngredientDispatchType} from './actionTypes';
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


const ingredientsSearch = (state: Nullable<Search<Ingredient>> = initialState.ingredientSearch,
                          action: IngredientDispatchType): Nullable<Search<Ingredient>> => {
    switch (action.type) {
        case actionTypes.CREATE_INGREDIENT:
            return state;

        default:
            return state;
    }
}


const ingredientsReducer = combineReducers<IIngredientState>({
    types: ingredientTypes,
    ingredientSearch: ingredientsSearch,
});


export default ingredientsReducer;
