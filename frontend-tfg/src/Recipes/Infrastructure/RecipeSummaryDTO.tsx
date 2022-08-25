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
}


export default RecipeSummaryDTO;
