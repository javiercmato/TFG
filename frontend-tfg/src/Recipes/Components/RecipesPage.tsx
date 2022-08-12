import CreateCategory from "./CreateCategory";
import {Col, Row} from "react-bootstrap";
import {useAppSelector} from "../../store";
import {userRedux} from "../../Users";

const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const RecipesPage = () => {
    const isAdminLoggedIn = useAppSelector(userRedux.selectors.selectIsAdmin);



    return (
        <Row>
            {/* Formulario para crear y mostrar las categorías */}
            {(isAdminLoggedIn) &&
                <Col md={4}>
                    <Row className={"gy-3"}>
                        <CreateCategory />
                    </Row>
                </Col>
            }

            {/* Columna con la lista de categorías existentes */}
            <Col>
                <Row>
                    CATEGORIES LIST
                </Row>
            </Col>
        </Row>
    )
}

export default RecipesPage;
