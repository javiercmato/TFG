import {default as createMockStore} from 'redux-mock-store';
import {initialState} from "../../store";
import {AuthenticatedUser, User, userRedux, userService} from '../../Users'
import thunk from "redux-thunk";
import {appRedux, ErrorDto} from "../../App";
import {generateValidUser} from "../TestUtils";

const middlewares = [thunk];
const mockStore = createMockStore(middlewares);


describe("User actions tests: ", () => {
    const signUpBackendSpy = jest.spyOn(userService, 'signUp');
    const loginBackendSpy = jest.spyOn(userService, 'login');
    const loginWithTokenBackendSpy = jest.spyOn(userService, 'loginWithServiceToken');
    const changePasswordBackendSpy = jest.spyOn(userService, 'changePassword');
    const findUserByNicknameBackendSpy = jest.spyOn(userService, 'findUserByNickname');

/* ******************** CONFIGURACIÓN PREVIA ******************** */
    afterAll(() => jest.restoreAllMocks());


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
            globalError: 'Error al realizar operación'
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
            appRedux.actions.error(backendError),
            appRedux.actions.loaded(),
        ]

        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(onSuccess).not.toHaveBeenCalled();
        expect(onErrors).toHaveBeenCalled();
        expect(store.getActions()).toEqual(expectedActions);
        expect(onErrors.mock.calls[0][0]).toEqual(backendError);
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
            userRedux.actions.loginAction(result),
            appRedux.actions.loaded(),
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(loginBackendSpy.mock.calls[0][0]).toEqual(inputUserData.nickname);
        expect(loginBackendSpy.mock.calls[0][1]).toEqual(inputUserData.password);
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Login action -> FAILURE', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Datos que introduce el usuario
        let inputUserData = {
            nickname: "foo",
            password: "password",
        };
        // Respuesta esperada por el backend
        let backendError: ErrorDto = {
            globalError: 'Error al realizar operación'
        };
        const onSuccess = jest.fn();
        const onErrors = jest.fn();
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let loginMockImplementation = (_nickname: string,
                                       _password: string,
                                       _onSuccess: CallbackFunction,
                                       _onErrors: CallbackFunction,
                                       _onReauthenticate: CallbackFunction) => _onErrors(backendError);
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
            appRedux.actions.error(backendError),
            appRedux.actions.loaded(),
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(onSuccess).not.toHaveBeenCalled();
        expect(onErrors).toHaveBeenCalled();
        expect(loginBackendSpy.mock.calls[0][0]).toEqual(inputUserData.nickname);
        expect(loginBackendSpy.mock.calls[0][1]).toEqual(inputUserData.password);
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Login with service Token action -> SUCCESS', () => {
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
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let loginWithTokenMockImplementation = (_onSuccess: CallbackFunction,
                                                _onReauthenticate: CallbackFunction) => _onSuccess(result);
        loginWithTokenBackendSpy.mockImplementation(loginWithTokenMockImplementation);

        const action = userRedux.actions.loginWithServiceTokenAsyncAction(onReauthenticate);
        const expectedActions = [
            userRedux.actions.loginAction(result),
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Login with service Token action -> FAILURE', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const onReauthenticate = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let loginWithTokenMockImplementation = (_onSuccess: CallbackFunction,
                                                _onReauthenticate: CallbackFunction) => _onReauthenticate({});
        loginWithTokenBackendSpy.mockImplementation(loginWithTokenMockImplementation);

        const action = userRedux.actions.loginWithServiceTokenAsyncAction(onReauthenticate);

        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(onReauthenticate).toBeCalled();
        expect(store.getActions()).toEqual([]);
    });

    test('Logout action -> SUCCESS', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Usuario actual con sesión ya iniciada
        let loggedUser: User = {
            userID: "00000000-0000-4000-8000-000000000000",
            role: "USER",
            isBannedByAdmin: false,
            registerdate: new Date(),
            name: "name",
            surname: "surname",
            email: `foo@email.es`,
            avatar: '',
            nickname: 'foo',

        };

        const action = userRedux.actions.logoutAsyncAction();
        const expectedActions = [
            userRedux.actions.logoutAction(),
        ];


        /* ******************** EJECUTAR ******************** */
        const mockInitialState = initialState;
        mockInitialState.users.user = loggedUser;
        const store = mockStore(mockInitialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(store.getActions()).toEqual(expectedActions);
        expect(store.getState()).toEqual(initialState);
    });

    test('Change Password action -> SUCCESS', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const userID = "00000000-0000-4000-8000-000000000000";
        // Datos que introduce el usuario
        let inputUserData = {
            oldPassword: "oldPassword",
            newPassword: "newPassword",
        };
        // Respuesta esperada por el backend
        let result = {};
        const onSuccess = jest.fn();
        const onErrors = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let changePasswordMockImplementation = (_userID: string,
                                                _oldPassword: string,
                                                _newPassword: string,
                                                _onSuccess: CallbackFunction,
                                                _onErrors: CallbackFunction) => _onSuccess(result);
        changePasswordBackendSpy.mockImplementation(changePasswordMockImplementation);

        const action = userRedux.actions.changePasswordAsyncAction(
            userID,
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
        expect(changePasswordBackendSpy.mock.calls[0][0]).toEqual(userID);
        expect(changePasswordBackendSpy.mock.calls[0][1]).toEqual(inputUserData.oldPassword);
        expect(changePasswordBackendSpy.mock.calls[0][2]).toEqual(inputUserData.newPassword);
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Change Password action -> FAILURE', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const userID = "00000000-0000-4000-8000-000000000000";
        // Datos que introduce el usuario
        let inputUserData = {
            oldPassword: "oldPassword",
            newPassword: "oldPassword",
        };
        // Respuesta esperada por el backend
        let backendError: ErrorDto = {
            globalError: 'Error al realizar operación'
        };
        const onSuccess = jest.fn();
        const onErrors = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let changePasswordMockImplementation = (_userID: string,
                                                _oldPassword: string,
                                                _newPassword: string,
                                                _onSuccess: CallbackFunction,
                                                _onErrors: CallbackFunction) => _onErrors(backendError);
        changePasswordBackendSpy.mockImplementation(changePasswordMockImplementation);

        const action = userRedux.actions.changePasswordAsyncAction(
            userID,
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
        expect(onSuccess).not.toHaveBeenCalled();
        expect(onErrors).toHaveBeenCalled();
        expect(changePasswordBackendSpy.mock.calls[0][0]).toEqual(userID);
        expect(changePasswordBackendSpy.mock.calls[0][1]).toEqual(inputUserData.oldPassword);
        expect(changePasswordBackendSpy.mock.calls[0][2]).toEqual(inputUserData.newPassword);
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Find User By Nickname action -> SUCCESS', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Datos que introduce el usuario
        let inputUserData = {
            nickname: 'user',
        };
        // Respuesta esperada por el backend
        let result = generateValidUser("00000000-0000-4000-8000-000000000000", inputUserData.nickname)
        const onSuccess = jest.fn();
        const onErrors = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let findUserByNicknameMockImplementation = (_nickname: string,
                                                    _onSuccess: CallbackFunction,
                                                    _onErrors: CallbackFunction) => _onSuccess(result);
        findUserByNicknameBackendSpy.mockImplementation(findUserByNicknameMockImplementation);

        const action = userRedux.actions.findUserByNicknameAsyncAction(inputUserData.nickname, onSuccess, onErrors);
        const expectedActions = [
            appRedux.actions.loading(),
            userRedux.actions.findUserByNicknameAction(result),
            appRedux.actions.loaded(),
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(findUserByNicknameBackendSpy.mock.calls[0][0]).toEqual(inputUserData.nickname);
        expect(store.getActions()).toEqual(expectedActions);
    });

    test('Find User By Nickname action -> FAILURE', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Datos que introduce el usuario
        let inputUserData = {
            nickname: 'user',
        };
        // Respuesta esperada por el backend
        let backendError: ErrorDto = {
            globalError: 'Error al realizar operación'
        };
        const onSuccess = jest.fn();
        const onErrors = jest.fn();

        // Reimplementación del backend para que se pasen por defecto los argumentos a testear
        let findUserByNicknameMockImplementation = (_nickname: string,
                                                    _onSuccess: CallbackFunction,
                                                    _onErrors: CallbackFunction) => _onErrors(backendError);
        findUserByNicknameBackendSpy.mockImplementation(findUserByNicknameMockImplementation);

        const action = userRedux.actions.findUserByNicknameAsyncAction(inputUserData.nickname, onSuccess, onErrors);
        const expectedActions = [
            appRedux.actions.loading(),
            appRedux.actions.error(backendError),
            appRedux.actions.loaded(),
        ];


        /* ******************** EJECUTAR ******************** */
        const store = mockStore(initialState);
        store.dispatch<any>(action);


        /* ******************** COMPROBAR ******************** */
        expect(onSuccess).not.toHaveBeenCalled();
        expect(onErrors).toHaveBeenCalled();
        expect(findUserByNicknameBackendSpy.mock.calls[0][0]).toEqual(inputUserData.nickname);
        expect(store.getActions()).toEqual(expectedActions);
    });

})
