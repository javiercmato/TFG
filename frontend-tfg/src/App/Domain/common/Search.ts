import defaultSearchCriteria, {SearchCriteria} from "./SearchCriteria";
import {Block} from "./Block";

interface Search<T> {
    criteria: SearchCriteria,
    result: Nullable<Block<T>>
}

const defaultSearch: Search<any> = {
    result: null,
    criteria: defaultSearchCriteria,
}

export default defaultSearch;
export type {Search};
