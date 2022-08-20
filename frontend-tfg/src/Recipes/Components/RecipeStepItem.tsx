import {CreateRecipeStepParamsDTO} from "../Infrastructure";
import {RecipeStep} from "../Domain";
import {Badge, Button, Col, Container, Row} from "react-bootstrap";
import {colText} from "./styles/recipeStepItem";
import {FaTrash} from "react-icons/fa";

interface Props {
    item: CreateRecipeStepParamsDTO | RecipeStep,
    onRemoveCallback: (stepIndex: number) => void,
}

const RecipeStepItem = ({item, onRemoveCallback}: Props) => {

    const handleClick = (e: any) => {
        e.preventDefault();

        onRemoveCallback(item.step);
    }

    return (
        <Container >
            <Row>
                <Col md={1} >
                    <Badge>
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

                <Col md={1}>
                    <Button variant="danger" onClick={handleClick}>
                        <FaTrash />
                    </Button>
                </Col>
            </Row>
        </Container>
    )
}

export type {Props as RecipeStepItemProps};
export default RecipeStepItem;
