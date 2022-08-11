/* ******************** Nombres de las acciones ******************** */

import {Ingredient, IngredientType} from "../../Domain";
import {Search} from "../../../App";

export const FIND_ALL_INGREDIENT_TYPES: string = 'ingredients/findAllIngredientTypes';
export const CREATE_INGREDIENT_TYPE : string = 'ingredients/createIngredientType';
export const CREATE_INGREDIENT : string = 'ingredients/createIngredient';
export const FIND_ALL_INGREDIENTS : string = 'ingredients/findAllIngredients';
export const FIND_INGREDIENTS : string = 'ingredients/findIngredients';
export const CLEAR_INGREDIENTS_SEARCH : string = 'ingredients/clearIngredientsSearch';



/* ******************** Tipos de las acciones ******************** */

export interface CreateIngredientTypeActionType {
    type: string,
    payload: IngredientType
}

export interface FindAllIngredientTypesActionType {
    type: string,
    payload: Array<IngredientType>
}

export interface CreateIngredientActionType {
    type: string,
    payload: Ingredient
}

export interface FindAllIngredientsActionType {
    type: string,
    payload: Search<Ingredient>
}

export interface FindIngredientsActionType {
    type: string,
    payload: Search<Ingredient>
}

export interface ClearIngredientsSearchActionType {
    type: string,
}


export type IngredientDispatchType = CreateIngredientTypeActionType
    | FindAllIngredientTypesActionType
    | CreateIngredientActionType
    | FindAllIngredientsActionType
    | FindIngredientsActionType
    | ClearIngredientsSearchActionType
;
