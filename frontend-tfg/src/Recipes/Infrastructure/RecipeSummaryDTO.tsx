import {UserSummaryDTO} from "../../Users";

interface RecipeSummaryDTO {
    id: string,
    categoryID: string,
    picture: Nullable<string>,
    name: string,
    description: Nullable<string>,
    isBannedByAdmin: boolean,
    duration: number,
    diners: Nullable<number>,
    ingredientsCount: number,
    averageRating: number,
    author: UserSummaryDTO,
}


export default RecipeSummaryDTO;
