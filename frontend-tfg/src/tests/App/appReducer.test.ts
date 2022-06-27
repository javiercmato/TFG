import {AppDispatchType} from "../../App/Application/state/actionTypes";
import {error, loaded, loading} from "../../App/Application/state/actions";
import {appInitialState, ErrorDto, IAppState} from "../../App";
import appReducer from "../../App/Application/state/reducer";

describe("App reducer tests: ", () => {
    test('Loading -> dispatch "Loading"', () => {
        /* ******************** EJECUTAR ******************** */
        const action: AppDispatchType = loading();
        const state: IAppState = appReducer(appInitialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.loading).toEqual(true);
    });

    test('Loading -> dispatch "Loaded"', () => {
        /* ******************** EJECUTAR ******************** */
        const action: AppDispatchType = loaded();
        const mockState: IAppState = appInitialState;
        mockState.loading = true;
        const state: IAppState = appReducer(mockState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.loading).toEqual(false);
    });

    test('Error -> dispatch "Error"', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const errorData: ErrorDto = {globalError: 'Soy un error'};
        /* ******************** EJECUTAR ******************** */
        const action: AppDispatchType = error(errorData);
        const state: IAppState = appReducer(appInitialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.loading).toEqual(false);
        expect(state.error).toEqual(errorData);
    });
});
