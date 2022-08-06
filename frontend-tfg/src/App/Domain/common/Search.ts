import {SearchCriteria} from "./SearchCriteria";
import {Block} from "./Block";

type Search<T> = {
    criteria?: SearchCriteria,
    result?: Block<T>
}

export type {Search};
