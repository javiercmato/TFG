/* https://redux.js.org/usage/usage-with-typescript#typing-the-usedispatch-hook */

import {useDispatch} from "react-redux";
import type {AppDispatch} from "../index";

export const useAppDispatch : () =>
    AppDispatch = useDispatch;

