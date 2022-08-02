import {RootState} from "../../../store";
import {IIngredientState} from "./IIngredientState";
import {IngredientType} from "../../Domain";


const getModuleState = (state: RootState) : IIngredientState => state.ingredients;

const getTypesModule = (state: RootState) : Array<IngredientType> => getModuleState(state).types;
