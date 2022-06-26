import {AuthenticatedUser, User} from "../../Users/Domain";
import {userService, usersInitialState} from '../../Users/Application';
import {SignUp} from "../../Users/Components";
import {generateValidUser, renderComponent} from "../TestUtils";
import {fireEvent, screen} from '@testing-library/react'


test("<SignUp />", () => {
    /* ******************** PREPARAR DATOS ******************** */
    const userID: string = '00000000-0000-4000-8000-000000000000';
    const nickname: string = 'Foo';
    const validUser: User = generateValidUser(userID, nickname);
    const result: AuthenticatedUser = {
        serviceToken: 'token',
        user: validUser
    }
    // Reimplementación del backend para que se pasen por defecto los argumentos a testear
    const signUpBackendSpy = jest.spyOn(userService, 'signUp');
    let signUpMockImplementation = (_userData: User,
                                    _onSuccess: CallbackFunction,
                                    _onErrors: CallbackFunction,
                                    _onReauthenticate: CallbackFunction) => _onSuccess(result);
    signUpBackendSpy.mockImplementation(signUpMockImplementation);

    /* ******************** EJECUTAR ******************** */
    const {history} = renderComponent(<SignUp />, usersInitialState);
    // Buscar los elementos a modificar
    const nameInput: HTMLElement = screen.getByLabelText('Nombre');

    // Insertar datos en componente
    fireEvent.change(nameInput, {target: {value: validUser.name}});

    /* ******************** COMPROBAR ******************** */
    expect(nameInput.nodeValue).toEqual(validUser.name);
    expect(history.location.pathname).toEqual('/');
});
