import {Link} from "react-router-dom";
import {Container, Nav, Navbar, NavbarBrand, NavDropdown} from "react-bootstrap";
import Dropdown from 'react-bootstrap/Dropdown'
import NavbarToggle from "react-bootstrap/NavbarToggle";
import NavbarCollapse from "react-bootstrap/NavbarCollapse";
import {FormattedMessage} from "react-intl";
import {GiHamburgerMenu} from "react-icons/gi";
import {FaHome} from "react-icons/fa";
import {useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {headerLink, navItem} from "./styles/header";

const Header = () => {
    const nickname: string = useAppSelector(userRedux.selectors.selectNickname);

    // Elementos del desplegable para acciones del usuario registrado
    const loggedUserActions = [
        {
            link: `/users/${nickname}`,
            i18nID: 'seeProfile',
            isDivided: false,
        },
        {
            link: '/profile',
            i18nID: 'editProfile',
            isDivided: false,
        },
        {
            link: `/changePassword`,
            i18nID: 'changePassword',
            isDivided: false,
        },
        {
            link: `/logout`,
            i18nID: 'logout',
            isDivided: true,
        }
    ];

    // Elementos del desplegable para acciones del usuario registrado
    const nonLoggedUserActions = [
        {
            link: `/signUp`,
            i18nID: 'signUp',
        },
        {
            link: `/login`,
            i18nID: 'login',
        },
    ];



    return (
        <Navbar expand="lg"
            bg="dark"
            variant="dark"
        >
            <Container>
                <NavbarBrand>
                    <Link to="/" style={headerLink}>
                        <FaHome size="2rem"/>
                    </Link>
                </NavbarBrand>

                <NavbarToggle type="button">
                    <GiHamburgerMenu />
                </NavbarToggle>

                {/* Contenido que se esconderĂ¡ cuando la pantalla se estreche */}
                <NavbarCollapse id="basic-navbar-nav">
                    {/* Elementos que se mostrarĂ¡n siempre */}
                    <Nav>
                    </Nav>

                    {/* Elementos a mostrar para un usuario registrado */}
                    {nickname ?
                        <Nav className="ms-auto">
                            <NavDropdown id="basic-nav-dropdown" title={nickname} align={"end"}>
                                {loggedUserActions.map( (action, index) => (
                                    <>
                                        {(action.isDivided) ? <Dropdown.Divider/> : null}
                                        <NavDropdown.Item>
                                            <Link to={action.link} style={headerLink} key={index}>
                                                <FormattedMessage id={`app.components.Header.userActions.${action.i18nID}`} />
                                            </Link>
                                        </NavDropdown.Item>
                                    </>
                                ))}
                            </NavDropdown>
                        </Nav>
                        :
                        // Elementos a mostrar para un usuario no registrado
                        <Nav className="ms-auto">
                            {nonLoggedUserActions.map( (action, index) => (
                                <Nav.Item style={navItem}>
                                    <Link to={action.link} style={headerLink} key={index}>
                                        <FormattedMessage id={`app.components.Header.${action.i18nID}`} />
                                    </Link>
                                </Nav.Item>
                            ))}
                        </Nav>
                    }
                </NavbarCollapse>
            </Container>
        </Navbar>
    )
};


export default Header;
