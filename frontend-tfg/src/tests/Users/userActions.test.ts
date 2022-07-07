import {default as createMockStore} from 'redux-mock-store';
import {initialState} from "../../store";
import {AuthenticatedUser, User, userRedux, userService} from '../../Users'
import thunk from "redux-thunk";
import {appRedux, ErrorDto} from "../../App";

const middlewares = [thunk];
const mockStore = createMockStore(middlewares);


describe("User actions tests: ", () => {
    const signUpBackendSpy = jest.spyOn(userService, 'signUp');
    const loginBackendSpy = jest.spyOn(userService, 'login');
    const changePasswordBackendSpy = jest.spyOn(userService, 'login');

/* ******************** CONFIGURACIÓN PREVIA ******************** */
    afterEach(() => jest.restoreAllMocks());


/* **************************** TESTS *************************** */
    test('Sign Up action -> SUCCESS', () => {
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
                userID: "00000000-0000-4000-8000-000000000000",
                role: "USER",
                isBannedByAdmin: false,
                registerdate: new Date(),
                // Datos introducidos por el usuario
                ...inputUserData
            }
        };
        const onSuccess = jest.fn();
        const onErrors = jest.fn();
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let signUpMockImplementation = (_userData: User,
                                        _onSuccess: CallbackFunction,
                                        _onErrors: CallbackFunction,
                                        _onReauthenticate: CallbackFunction) => _onSuccess(result);
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

    test('Sign Up action -> FAILURE', () => {
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
        let backendError: ErrorDto = {
            globalError: 'Soy un error'
        };
        const onSuccess = jest.fn();
        const onErrors = jest.fn();
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let signUpMockImplementation = (_userData: User,
                                        _onSuccess: CallbackFunction,
                                        _onError: CallbackFunction,
                                        _onReauthenticate: CallbackFunction) => _onError(backendError);
        signUpBackendSpy.mockImplementation(signUpMockImplementation);

        const action = userRedux.actions.signUpAsyncAction(inputUserData, onSuccess, onErrors, onReauthenticate);
        const expectedActions = [
            appRedux.actions.loading(),
        ]

        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(onSuccess).not.toHaveBeenCalled();
        //expect(onErrors).toHaveBeenCalled();
        expect(store.getActions()).toEqual(expectedActions);
        //expect(onErrors.mock.calls[0][0]).toEqual(backendError);
    });

    test('Login action -> SUCCESS', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Datos que introduce el usuario
        let inputUserData = {
            nickname: "foo",
            password: "password",
        };
        // Respuesta esperada por el backend
        let result: AuthenticatedUser = {
            serviceToken: 'token',
            user: {
                // Datos generados en backend
                userID: "00000000-0000-4000-8000-000000000000",
                role: "USER",
                isBannedByAdmin: false,
                registerdate: new Date(),
                name: "name",
                surname: "surname",
                email: `foo@email.es`,
                // Datos introducidos por el usuario
                ...inputUserData
            }
        };
        const onSuccess = jest.fn();
        const onErrors = jest.fn();
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let loginMockImplementation = (_nickname: string,
                                       _password: string,
                                       _onSuccess: CallbackFunction,
                                       _onErrors: CallbackFunction,
                                       _onReauthenticate: CallbackFunction) => _onSuccess(result);
        loginBackendSpy.mockImplementation(loginMockImplementation);

        const action = userRedux.actions.loginAsyncAction(
            inputUserData.nickname,
            inputUserData.password,
            onSuccess,
            onErrors,
            onReauthenticate
        );
        const expectedActions = [
            appRedux.actions.loading(),
            //userRedux.actions.loginAction(result),
            //appRedux.actions.loaded(),
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        //expect(loginBackendSpy.mock.calls[0][0]).toEqual(inputUserData.nickname);
        //expect(loginBackendSpy.mock.calls[0][1]).toEqual(inputUserData.password);
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Change Password action -> SUCCESS', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Datos que introduce el usuario
        let inputUserData = {
            oldPassword: "oldPassword",
            newPassword: "newPassword",
        };
        // Respuesta esperada por el backend
        let result = {};
        const onSuccess = jest.fn();
        const onErrors = jest.fn();
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let changePasswordMockImplementation = (_oldPassword: string,
                                                _newPassword: string,
                                                _onSuccess: CallbackFunction,
                                                _onErrors: CallbackFunction) => _onSuccess(result);
        changePasswordBackendSpy.mockImplementation(changePasswordMockImplementation);

        const action = userRedux.actions.changePasswordAsyncAction(
            "00000000-0000-4000-8000-000000000000",
            inputUserData.oldPassword,
            inputUserData.newPassword,
            onSuccess,
            onErrors
        );
        const expectedActions = [
            userRedux.actions.changePasswordAction()
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        //expect(changePasswordBackendSpy.mock.calls[0][0]).toEqual(inputUserData.oldPassword);
        //expect(changePasswordBackendSpy.mock.calls[0][1]).toEqual(inputUserData.newPassword);
        expect(store.getActions()).toEqual(expectedActions);
    });

})
