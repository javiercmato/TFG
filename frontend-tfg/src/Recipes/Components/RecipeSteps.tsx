import {RecipeStep} from "../Domain";
import {Alert, Badge, Col, Container, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {colText} from "./styles/recipeStepItem";

interface Props {
    steps: Array<RecipeStep>
}

const RecipeSteps = ({steps}: Props) => {


    if (steps.length === 0) {
        return (
            <Alert variant="info">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    return (
        <Container >
            {steps.map((item) =>
                <Row key={item.step}>
                    <Col md={1} >
                        <Badge bg="dark">
                            <h5>{item.step + 1}</h5>
                        </Badge>
                    </Col>

                    <Col className="my-auto border border-primary"
                         style={colText}
                    >
                        <span>
                            {item.text}
                        </span>
                    </Col>

                </Row>
            )}
        </Container>
    )
}

export type {Props as RecipeStepsProps};
export default RecipeSteps;
