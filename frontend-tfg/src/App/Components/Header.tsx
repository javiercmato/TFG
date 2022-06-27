import {Link} from "react-router-dom";
import {Container, Nav, Navbar, NavbarBrand, NavDropdown} from "react-bootstrap";
import NavbarToggle from "react-bootstrap/NavbarToggle";
import NavbarCollapse from "react-bootstrap/NavbarCollapse";
import {FormattedMessage} from "react-intl";
import {GiHamburgerMenu} from "react-icons/gi";
import {FaHome} from "react-icons/fa";
import {useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {headerLink} from "./styles/header";

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

                {/*Contenido que se esconderá cuando la pantalla se estreche */}
                <NavbarCollapse id="basic-navbar-nav">
                    <Nav>
                        {nickname ?
                            // Elementos a mostrar para un usuario registrado
                            <NavDropdown id="basic-nav-dropdown" title={nickname}>
                                {/*// TODO: Meter enlaces a acciones (cambiar contraseña, ver perfil, etc)*/}
                            </NavDropdown>
                            :
                            // Elementos a mostrar para un usuario no registrado
                            <Link to="/signUp" style={headerLink}>
                                <FormattedMessage id="app.components.Header.signUp" />
                            </Link>
                        }
                    </Nav>
                </NavbarCollapse>
            </Container>
        </Navbar>
    )
};


export default Header;
