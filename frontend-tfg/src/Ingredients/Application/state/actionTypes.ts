/* ******************** Nombres de las acciones ******************** */

import {Ingredient, IngredientType} from "../../Domain";
import {Search} from "../../../App";

export const FIND_ALL_INGREDIENT_TYPES: string = 'ingredients/findAllIngredientTypes';
export const CREATE_INGREDIENT_TYPE : string = 'ingredients/createIngredientType';
export const CREATE_INGREDIENT : string = 'ingredients/createIngredient';
export const FIND_ALL_INGREDIENTS : string = 'ingredients/findAllIngredients';
export const FIND_INGREDIENTS_BY_TYPE : string = 'ingredients/findIngredientsByType';
export const FIND_INGREDIENTS_BY_NAME : string = 'ingredients/findIngredientsByName';
export const FIND_INGREDIENTS_BY_NAME_AND_TYPE : string = 'ingredients/findIngredientsByNameAndType';



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

export interface FindIngredientsByTypeActionType {
    type: string,
    payload: Search<Ingredient>
}

export interface FindIngredientsByNameActionType {
    type: string,
    payload: Search<Ingredient>
}

export interface FindIngredientsByNameAndTypeActionType {
    type: string,
    payload: Search<Ingredient>
}


export type IngredientDispatchType = CreateIngredientTypeActionType
    | FindAllIngredientTypesActionType
    | CreateIngredientActionType
    | FindAllIngredientsActionType
    | FindIngredientsByTypeActionType
    | FindIngredientsByNameActionType
    | FindIngredientsByNameAndTypeActionType
;
