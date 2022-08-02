import {combineReducers} from '@reduxjs/toolkit'
import * as actionTypes from './actionTypes';
import {FindAllIngredientTypesActionType, IngredientDispatchType} from './actionTypes';
import {IIngredientState, initialState} from "./IIngredientState";
import {IngredientType} from "../../Domain";

const ingredientTypes = (state: Array<IngredientType> = initialState.types,
                         action: IngredientDispatchType): Array<IngredientType> => {
    switch (action.type) {
        case actionTypes.FIND_ALL_INGREDIENT_TYPES:
            return (action as FindAllIngredientTypesActionType).payload;


        default:
            return state;
    }
}



const ingredientsReducer = combineReducers<IIngredientState>({
    types: ingredientTypes
});


export default ingredientsReducer;
