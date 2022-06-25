import {User, AuthenticatedUser} from "../../Users";
import {UserDispatchType} from "../../Users/Application/state/actionTypes";
import {signUpAction} from "../../Users/Application/state/actions";
import rootReducer, {RootStateType} from "../../store/rootReducer";
import {initialState} from "../../store";

/** Genera datos de un usuario en función de su ID (si es administrador o no) */
const generateValidUser = (userID: string, nickname: string) : User => {
    const password: string = "password";
    const name: string = "name";
    const surname: string = "surname";
    const email: string = `${nickname}@email.es`;
    const avatar: string = '';
    const role: string = 'USER';
    const isBannedByAdmin: boolean = false;
    const registerdate: Date = new Date();

    return {
        userID,
        nickname,
        password,
        name,
        surname,
        email,
        avatar,
        role,
        isBannedByAdmin,
        registerdate
    };
}

describe("User reducer tests: ", () => {
    test('Sign Up reducer', () => {
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
