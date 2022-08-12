import {Category} from "../../Domain";

/* ******************** Nombres de las acciones ******************** */

export const CREATE_CATEGORY: string = 'recipes/createCategory';
export const GET_CATEGORIES: string = 'recipes/findCategories';


/* ******************** Tipos de las acciones ******************** */

export interface CreateCategoryActionType {
    type: string,
    payload: Category
}

export interface GetCategoriesActionType {
    type: string,
    payload: Array<Category>
}


export type RecipeDispatchType = CreateCategoryActionType
    | GetCategoriesActionType
;
