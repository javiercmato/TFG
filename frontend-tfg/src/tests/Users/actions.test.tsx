import {default as createMockStore} from 'redux-mock-store';
import {RootState, initialState, AppThunk, AppDispatch} from "../../store";
import {AuthenticatedUser, User, userService, userRedux} from '../../Users'
import {UserDispatchType} from "../../Users/Application/state/actionTypes";
import thunk from "redux-thunk";
import {appRedux} from "../../App";

const middlewares = [thunk];
const mockStore = createMockStore(middlewares);


describe("User actions tests: ", () => {
/* ******************** CONFIGURACIÓN PREVIA ******************** */
    const signUpBackendSpy = jest.spyOn(userService, 'signUp');
    afterEach(() => jest.restoreAllMocks())


    test('Sign Up action', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Datos que introduce el usuario
        let inputUserData: User = {
            nickname: "foo",
            password: "password",
            name: "name",
            surname: "surname",
            email: `foo@email.es`,
            avatar: ''
        };

        // Respuesta esperada por el backend
        let result: AuthenticatedUser = {
            serviceToken: 'token',
            user: {
                // Datos generados en backend
                "userID": "00000000-0000-4000-8000-000000000000",
                "role": "USER",
                "isBannedByAdmin": false,
                "registerdate": new Date(),
                // Datos introducidos por el usuario
                ...inputUserData
            }
        };
        const onSuccess = jest.fn();
        const onErrors = jest.fn();
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let signUpMockImplementation = (userData: User,
                                        onSuccess: CallbackFunction,
                                        onErrors: CallbackFunction,
                                        onReauthenticate: CallbackFunction) => onSuccess(result);
        signUpBackendSpy.mockImplementation(signUpMockImplementation);

        const action = userRedux.actions.signUpAsyncAction(inputUserData, onSuccess, onErrors, onReauthenticate);
        const expectedActions = [
            appRedux.actions.loading(),
            userRedux.actions.signUpAction(result),
            appRedux.actions.loaded(),
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(signUpBackendSpy.mock.calls[0][0]).toEqual(inputUserData);
        expect(store.getActions()).toEqual(expectedActions);
    });
})
