import CreateRecipeStepParamsDTO from "./CreateRecipeStepParamsDTO";
import CreateRecipePictureParamsDTO from "./CreateRecipePictureParamsDTO";
import CreateRecipeIngredientParamsDTO from "./CreateRecipeIngredientParamsDTO";

interface CreateRecipeParamsDTO {
    name: string,
    description: Nullable<string>,
    duration: number,
    diners: number,
    authorID: string,
    categoryID: string,
    steps: Array<CreateRecipeStepParamsDTO>,
    pictures: Nullable<Array<CreateRecipePictureParamsDTO>>,
    ingredients: Array<CreateRecipeIngredientParamsDTO>
}

export default CreateRecipeParamsDTO;
