/* https://redux.js.org/usage/usage-with-typescript#typing-the-usedispatch-hook */

import {TypedUseSelectorHook, useSelector} from "react-redux";
import {RootStateType} from "../rootReducer";

export const useAppSelector: TypedUseSelectorHook<RootStateType> = useSelector;
