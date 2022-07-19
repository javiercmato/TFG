import createMockStore from "redux-mock-store";
import {appInitialState, appRedux, ErrorDto} from "../../App";
import {initialState} from "../../store";

const mockStore = createMockStore();


describe("App actions tests: ", () => {
    test('Loading action', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const action = appRedux.actions.loading();
        const expectedActions = [
            appRedux.actions.loading()
        ];

        /* ******************** EJECUTAR ******************** */
        const store = mockStore(appInitialState);
        store.dispatch(action);

        /* ******************** COMPROBAR ******************** */
        expect(store.getActions()).toEqual(expectedActions)});

    test('Loaded action', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const action = appRedux.actions.loaded();
        const expectedActions = [
            appRedux.actions.loaded()
        ];

        /* ******************** EJECUTAR ******************** */
        let mockInitialState = initialState;
        mockInitialState.app.loading = true;
        const store = mockStore(mockInitialState);
        store.dispatch(action);

        /* ******************** COMPROBAR ******************** */
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Error action', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const errorData: ErrorDto = {globalError: 'Soy un error'};
        const action = appRedux.actions.error(errorData);
        const expectedActions = [
            appRedux.actions.error(errorData)
        ];

        /* ******************** EJECUTAR ******************** */
        const store = mockStore(appInitialState);
        store.dispatch(action);

        /* ******************** COMPROBAR ******************** */
        expect(store.getActions()).toEqual(expectedActions);
    });
});
