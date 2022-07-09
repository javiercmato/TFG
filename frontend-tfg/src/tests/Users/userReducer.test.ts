import {AuthenticatedUser, User} from "../../Users";
import {UserDispatchType} from "../../Users/Application/state/actionTypes";
import {
    banUserAction,
    changePasswordAction,
    findUserByNicknameAction,
    loginAction,
    logoutAction,
    signUpAction,
    updateProfileAction
} from "../../Users/Application/state/actions";
import rootReducer, {RootStateType} from "../../store/rootReducer";
import {initialState} from "../../store";
import {generateValidUser, NICKNAME, USER_ID} from "../TestUtils";


describe("User reducer tests: ", () => {
    test('User -> dispatch signUp', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Respuesta esperada por el backend
        const authUser : AuthenticatedUser = {
            serviceToken: 'token',
            user: generateValidUser(USER_ID, NICKNAME)
        }

        /* ******************** EJECUTAR ******************** */
        const action: UserDispatchType = signUpAction(authUser);
        const state: RootStateType = rootReducer(initialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.user).toEqual(authUser.user);
    });

    test('User -> dispatch login', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Respuesta esperada por el backend
        const authUser : AuthenticatedUser = {
            serviceToken: 'token',
            user: generateValidUser(USER_ID, NICKNAME)
        }

        /* ******************** EJECUTAR ******************** */
        const action: UserDispatchType = loginAction(authUser);
        const state: RootStateType = rootReducer(initialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.user).toEqual(authUser.user);
    });

    test('User -> dispatch logout', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const nickname: string = 'Foo';

        /* ******************** EJECUTAR ******************** */
        const action: UserDispatchType = logoutAction();
        const state: RootStateType = rootReducer(initialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.user).toEqual(null);
    })

    test('User -> dispatch changePassword', () => {
        const action: UserDispatchType = changePasswordAction();
        const state: RootStateType = rootReducer(initialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.user).toEqual(state.users.user);
    });

    test('User -> dispatch updateProfile', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const mockInitialState = initialState;
        mockInitialState.users.user = generateValidUser(USER_ID, NICKNAME);
        // Respuesta esperada por el backend
        const result : User = generateValidUser(USER_ID, NICKNAME);
        result.name += 'X';
        result.surname += 'X';
        result.email = 'X' + result.email;

        /* ******************** EJECUTAR ******************** */
        const action: UserDispatchType = updateProfileAction(result);
        const state: RootStateType = rootReducer(mockInitialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.user).toEqual(result);
    });



    test('UserSearch -> dispatch findUserByNickname', () => {
        /* ******************** PREPARAR DATOS ******************** */
        // Respuesta esperada por el backend
        const result : User = generateValidUser(USER_ID, NICKNAME);

        /* ******************** EJECUTAR ******************** */
        const action: UserDispatchType = findUserByNicknameAction(result);
        const state: RootStateType = rootReducer(initialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.userSearch).toBe(result);
    });

    test('UserSearch -> dispatch banUser', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const mockInitialState = initialState;
        mockInitialState.users.userSearch = generateValidUser(USER_ID, NICKNAME);
        // Respuesta esperada por el backend
        const result : boolean = true;

        /* ******************** EJECUTAR ******************** */
        const action: UserDispatchType = banUserAction(result);
        const state: RootStateType = rootReducer(mockInitialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.userSearch?.isBannedByAdmin).toBeTruthy();
    });

});
