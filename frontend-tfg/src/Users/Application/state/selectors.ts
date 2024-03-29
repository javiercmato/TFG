import {RootState} from "../../../store";
import {IUserState} from "./IUserState";
import {PrivateList, User} from "../../Domain";
import {RecipeSummaryDTO} from "../../../Recipes";
import PrivateListSummaryDTO from "../../Infrastructure/PrivateListSummaryDTO";

const getModuleState = (state: RootState) : IUserState => state.users;

const getUserModule = (state: RootState) : Nullable<User> => getModuleState(state).user;

const getUserSearchModule = (state: RootState) : Nullable<User> => getModuleState(state).userSearch;

const getPrivateListsModule = (state: RootState) : Array<PrivateListSummaryDTO> => getModuleState(state).privateLists;

export const getPrivateListDetailsModule = (state: RootState) : Nullable<PrivateList> => getModuleState(state).privateListDetails;

/* ******************** DATOS DE USUARIO ******************** */

export const isLoggedIn = (state: RootState) : boolean => getUserModule(state) != null;

export const selectIsAdmin = (state: RootState) : boolean => (getUserModule(state)?.role! === 'ADMIN');

export const selectCurrentUser = (state: RootState) : Nullable<User> => getUserModule(state);

export const selectNickname = (state: RootState) : string => getUserModule(state)?.nickname!;

export const selectUserID = (state: RootState) : string => getUserModule(state)?.userID!;

export const isBannedByAdmin = (state: RootState) : boolean => getUserModule(state)?.isBannedByAdmin!;


/* ******************** DATOS DE BÚSQUEDA DE USUARIO ******************** */

export const selectUserSearch = (state: RootState) : User => getUserSearchModule(state)!;

export const isUserSearchAdmin = (state: RootState) : boolean => getUserSearchModule(state)?.role! === 'ADMIN';

export const isUserSearchBannedByAdmin = (state: RootState) : boolean => getUserSearchModule(state)?.isBannedByAdmin!;

export const isUserSearchFollowedByUser = (state: RootState) : boolean => getUserSearchModule(state)?.isFollowedByUser!;

/* ******************** DATOS DE LISTAS PRIVADAS ******************** */

export const selectPrivateLists = (state: RootState) : Array<PrivateListSummaryDTO> => getPrivateListsModule(state);




/* ******************** DATOS DE LA LISTA PRIVADA SELECCIONADA ******************** */
export const selectRecipeDetailsFromList = (recipeID: string, list: Array<RecipeSummaryDTO>) => {
    if (!list) return null;

    const recipe = list.find((r: RecipeSummaryDTO) => r.id === recipeID);
    if (!recipe) return null;

    return recipe;
}
