import {AuthenticatedUser} from "../../Users";
import {UserDispatchType} from "../../Users/Application/state/actionTypes";
import {signUpAction} from "../../Users/Application/state/actions";
import rootReducer, {RootStateType} from "../../store/rootReducer";
import {initialState} from "../../store";
import {generateValidUser} from "../TestUtils";


describe("User reducer tests: ", () => {
    test('User -> dispatch signUp', () => {
        /* ******************** PREPARAR DATOS ******************** */
        const userID: string = '00000000-0000-4000-8000-000000000000';
        const nickname: string = 'Foo';
        // Respuesta esperada por el backend
        const authUser : AuthenticatedUser = {
            serviceToken: 'token',
            user: generateValidUser(userID, nickname)
        }

        /* ******************** EJECUTAR ******************** */
        const action: UserDispatchType = signUpAction(authUser);
        const state: RootStateType = rootReducer(initialState, action);

        /* ******************** COMPROBAR ******************** */
        expect(state.users.user).toEqual(authUser.user);
    });
});
