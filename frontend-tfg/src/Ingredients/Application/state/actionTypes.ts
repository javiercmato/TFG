/* ******************** Nombres de las acciones ******************** */

import {IngredientType} from "../../Domain";

export const FIND_ALL_INGREDIENT_TYPES: string = 'ingredients/findAllIngredientTypes';
export const CREATE_INGREDIENT_TYPE : string = 'ingredients/createIngredientType';


/* ******************** Tipos de las acciones ******************** */

export interface CreateIngredientTypeActionType {
    type: string,
    payload: IngredientType
}

export interface FindAllIngredientTypesActionType {
    type: string,
    payload: Array<IngredientType>
}



export type IngredientDispatchType = CreateIngredientTypeActionType
    | FindAllIngredientTypesActionType;
