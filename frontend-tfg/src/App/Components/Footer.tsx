import {Col, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import {FaCloud, FaGithub} from "react-icons/fa";
import {footer, footerColStyle, footerRowStyle} from "./styles/footer";
import ExternalLink from "./ExternalLink";
import {Fragment} from "react";

interface FooterLink {
    name: string,
    link: string,
    icon: JSX.Element
}

const Footer = () => {
    const footerLinks : Array<FooterLink> =  [
        {name: "Github", link: "https://github.com/javiercmato/TFG", icon: <FaGithub/>},
        {name: "SonarCloud (frontend)", link: "https://sonarcloud.io/project/overview?id=javiercmato_TFG", icon: <FaCloud />},
        {name: "SonarCloud (backend)", link: "https://sonarcloud.io/project/overview?id=javiercmato_TFG", icon: <FaCloud />},
    ]

    return (
        <footer style={footer}>
            <Row style={footerRowStyle}>
                {/* Enlaces */}
                <Col style={footerColStyle}>
                    <ListGroup horizontal variant="flush">
                        {footerLinks.map((item) =>
                            <ListGroupItem variant="dark" >
                                <Fragment>
                                    {item.icon}
                                    <br />
                                    <ExternalLink link={item.link} text={item.name} />
                                </Fragment>
                            </ListGroupItem>
                        )}
                    </ListGroup>
                </Col>
            </Row>
        </footer>
    )
}


export default Footer;
