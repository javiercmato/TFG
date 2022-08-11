const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

/** Posibles criterios para realizar una búsqueda con parámetros */
interface SearchCriteria {
    name: Nullable<string>,
    type: Nullable<string>,
    page: number,
    pageSize: number,
}

const defaultSearchCriteria: SearchCriteria = {
    page: 0,
    pageSize: DEFAULT_PAGE_SIZE,
    name: null,
    type: null
}

export type {SearchCriteria};
export default defaultSearchCriteria;
