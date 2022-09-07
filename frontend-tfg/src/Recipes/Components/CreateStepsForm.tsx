import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {ChangeEvent, useEffect, useState} from "react";
import {CreateRecipeStepParamsDTO} from "../Infrastructure";
import {FormattedMessage, useIntl} from "react-intl";
import RecipeStepItem, {RecipeStepItemProps} from "./RecipeStepItem";

interface Props {
    onAddCallback: (stepParams: Array<CreateRecipeStepParamsDTO>) => void,
    onRemoveCallback: (stepIndex: number) => void,
}

const CreateStepsForm = ({onAddCallback, onRemoveCallback}: Props) => {
    const intl = useIntl();
    const [stepParams, setStepParams] = useState<Array<CreateRecipeStepParamsDTO>>([]);
    const [stepText, setStepText] = useState<string>('');
    const [stepIndex, setStepIndex] = useState<number>(0);


    const handleAddClick = () => {
        // Crea el paso de receta y lo añade
        let param: CreateRecipeStepParamsDTO = {
            step: stepIndex,
            text: stepText
        }
        setStepParams([...stepParams, param]);

        // Aumenta el índice para el siguiente paso
        setStepIndex((value) => value + 1);

        // Limpia el input
        setStepText('');
    }


    // Devuelve los pasos al padre cada vez que haya un cambio
    useEffect(() => {
        onAddCallback(stepParams);
    }, [stepParams])

    return (
        <Container>
            {/* Fila para crear el paso */}
            <Row>
                <Col md={10}>
                    <Form.Control
                        as="textarea"
                        rows={2}
                        cols={50}
                        value={stepText}
                        onChange={(e: ChangeEvent<HTMLInputElement>) => setStepText(e.target.value)}
                        placeholder={intl.formatMessage({id: 'recipes.components.CreateStepsForm.placeholder'})}
                    />
                </Col>

                <Col sm={2} className="my-auto">
                    <Button
                        disabled={stepText === ''}
                        onClick={handleAddClick}
                    >
                        <FormattedMessage id="common.buttons.add" />
                    </Button>
                </Col>
            </Row>

            {/* Fila para mostrar los pasos creados */}
            <Row>
                {stepParams.map( (step) => {
                    let props: RecipeStepItemProps = {
                        item: step,
                        onRemoveCallback: onRemoveCallback,
                    };

                    return (
                        <RecipeStepItem {...props} key={step.step} />
                    )}
                )}
            </Row>
        </Container>
    )
}


export type {Props as CreateStepsFormProps};
export default CreateStepsForm;
