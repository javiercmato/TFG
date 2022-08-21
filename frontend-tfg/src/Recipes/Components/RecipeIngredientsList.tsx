import {RecipeIngredient} from "../Domain";
import {FormattedMessage} from "react-intl";
import {Alert, Col, ListGroup, ListGroupItem, Row} from "react-bootstrap";

interface Props {
    ingredients: Array<RecipeIngredient>
}

const RecipeIngredientsList = ({ingredients}: Props) => {

    if (ingredients.length === 0) {
        return (
            <Alert variant="info">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    return (
        <ListGroup>
            {ingredients.map((ingredient) =>
                <ListGroupItem key={ingredient.id}>

                    <Row>
                        <Col md={6}>
                            {ingredient.name}
                        </Col>
                        <Col md={2}>
                            {ingredient.quantity}
                        </Col>
                        <Col md={4}>
                            {ingredient.measureUnit}
                        </Col>
                    </Row>
                </ListGroupItem>
            )}
        </ListGroup>
    )
}

export type {Props as RecipeIngredientsListProps};
export default RecipeIngredientsList;
