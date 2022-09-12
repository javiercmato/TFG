const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

/** Posibles criterios para realizar una búsqueda con parámetros */
interface SearchCriteria {
    name: Nullable<string>,
    type: Nullable<string>,
    category: Nullable<string>,
    ingredients: Nullable<Array<string>>,
    page: number,
    pageSize: number,
    recipeID: Nullable<string>,
    userID: Nullable<string>,
}

const defaultSearchCriteria: SearchCriteria = {
    page: 0,
    pageSize: DEFAULT_PAGE_SIZE,
    name: null,
    type: null,
    category: null,
    ingredients: null,
    recipeID: null,
    userID: null,
}

export type {SearchCriteria};
export default defaultSearchCriteria;
