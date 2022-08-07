import {Col, Row} from "react-bootstrap";
import CreateIngredientType from "./CreateIngredientType";
import IngredientTypesList from "./IngredientTypesList";

const IngredientsPage = () => {

    return (
        <Row>
            {/* Formulario para crear y mostrar los tipos de ingredientes */}
            <Col md={4}>
                <CreateIngredientType />
                <IngredientTypesList />
            </Col>

            {/* Columna para buscar ingredientes */}
            <Col>
            </Col>
        </Row>
    )
};

export default IngredientsPage;
