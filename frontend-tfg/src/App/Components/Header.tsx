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

                {/* Contenido que se esconderá cuando la pantalla se estreche */}
                <NavbarCollapse id="basic-navbar-nav">
                    {/* Elementos que se mostrarán siempre */}
                    <Nav>
                    </Nav>

                    {/* Elementos a mostrar para un usuario registrado */}
                    {nickname ?
                        <Nav className="ms-auto">
                            <NavDropdown id="basic-nav-dropdown" title={nickname} align={"end"}>
                                <NavDropdown.Item>
                                    <Link to={`/users/${nickname}`} style={headerLink}>
                                        <FormattedMessage id="app.components.Header.userActions.seeProfile" />
                                    </Link>
                                </NavDropdown.Item>
                                <NavDropdown.Item>
                                    <Link to="/changePassword" style={headerLink}>
                                        <FormattedMessage id="app.components.Header.userActions.changePassword" />
                                    </Link>
                                </NavDropdown.Item>
                                <Dropdown.Divider />
                                <NavDropdown.Item>
                                    <Link to="/logout" style={headerLink}>
                                        <FormattedMessage id="app.components.Header.userActions.logout" />
                                    </Link>
                                </NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                        :
                        // Elementos a mostrar para un usuario no registrado
                        <Nav className="ms-auto">
                            <Nav.Item style={navItem}>
                                <Link to="/login" style={headerLink}>
                                    <FormattedMessage id="app.components.Header.login" />
                                </Link>
                            </Nav.Item>
                            <Nav.Item style={navItem}>
                                <Link to="/signUp" style={headerLink}>
                                    <FormattedMessage id="app.components.Header.signUp" />
                                </Link>
                            </Nav.Item>
                        </Nav>
                    }
                </NavbarCollapse>
            </Container>
        </Navbar>
    )
};


export default Header;
