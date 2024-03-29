import {RootState} from "../../../store";
import {IRecipeState} from "./IRecipeState";
import {Category, Recipe} from "../../Domain";
import {Block, Search, SearchCriteria} from "../../../App";
import RecipeSummaryDTO from "../../Infrastructure/RecipeSummaryDTO";
import {Comment} from "../../../Social";


const getModuleState = (state: RootState): IRecipeState => state.recipes;

/* ******************** DATOS DE CATEGORIAS ******************** */

export const selectCategories = (state: RootState) : Array<Category> => getModuleState(state).categories;

/** Devuelve el nombre de la categoría a partir de las categorías cargadas en memoria */
export const selectCategoryName = (categories: Array<Category>, categoryId: string) : string => {
    if (!categories) return '';

    const category = categories.find((c: Category) => c.id === categoryId);
    if (!category) return '';

    return category.name;
}

/* ******************** DATOS DE RECETA ******************** */

export const selectRecipe = (state: RootState) : Nullable<Recipe> => getModuleState(state).recipe;

export const getRecipeSearch = (state: RootState) : Search<RecipeSummaryDTO> => getModuleState(state).recipeSearch;

export const selectSearchCriteria = (state: RootState) : SearchCriteria => getRecipeSearch(state).criteria;

export const selectSearchResultBlock = (state: RootState) : Nullable<Block<RecipeSummaryDTO>> => getRecipeSearch(state).result;

export const isBannedByAdmin = (state: RootState) : boolean => selectRecipe(state)?.isBannedByAdmin!;

export const selectAverageRating = (state: RootState) : number => selectRecipe(state)?.averageRating!;

export const selectTotalVotes = (state: RootState) : number => selectRecipe(state)?.totalVotes!;


/* ******************** DATOS DE COMENTARIOS EN RECETA ******************** */

export const selectComment = (comments: Array<Comment>, commentID: string) : Nullable<Comment> => {
    const comment = comments.find((c: Comment) => c.id === commentID);

    return (comment ?? null);
}
