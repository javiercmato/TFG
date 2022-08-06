import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";

const IngredientsPage = () => {

    return (
        <Row>
            {/* Columna para mostrar lista de ingredientes y botón para crear nuevo tipo */}
            <Col md={4}>
                <CreateIngredientType />
            </Col>

            {/* Columna para buscar ingredientes */}
            <Col>
            </Col>
        </Row>
    )
};

export default IngredientsPage;
