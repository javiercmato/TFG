import {useAppDispatch, useAppSelector} from "../../store";
import {useNavigate} from "react-router-dom";
import {ErrorDto, Errors} from "../../App";
import {ChangeEvent, FormEvent, useState} from "react";
import {Button, Card, Col, Form, FormGroup, Row} from "react-bootstrap";
import {FormattedMessage, useIntl} from "react-intl";
import {card, descriptionTextarea} from './styles/createRecipeForm';
import CategorySelector from "./CategorySelector";
import RecipeIngredientsForm, {RecipeIngredientsFormProps} from "./RecipeIngredients/RecipeIngredientsForm";
import {
    CreateRecipeIngredientParamsDTO,
    CreateRecipeParamsDTO,
    CreateRecipePictureParamsDTO,
    CreateRecipeStepParamsDTO
} from "../Infrastructure";
import RecipePicturesForm, {RecipePicturesFormProps} from "./RecipePicturesForm";
import {userRedux} from "../../Users";


const CreateRecipeForm = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const intl = useIntl();
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const [name, setName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [duration, setDuration] = useState<number>(0);
    const [diners, setDiners] = useState<number>(0);
    const [categoryID, setCategoryID] = useState<string>('');
    const [recipeIngredientsParams, setRecipeIngredientsParams] = useState<Array<CreateRecipeIngredientParamsDTO>>([]);
    const [recipeStepsParams, setRecipeStepsParams] = useState<Array<CreateRecipeStepParamsDTO>>([]);
    const [recipePicturesParams, setRecipePicturesParams] = useState<Nullable<Array<CreateRecipePictureParamsDTO>>>(null);
    const currentUserID = useAppSelector(userRedux.selectors.selectUserID);
    let formRef: HTMLFormElement;

    const handleCategorySelectorChange = (e: ChangeEvent<HTMLSelectElement>) => {
        e.preventDefault();

        setCategoryID(e.target.value);
    }

    /** Añade los ingredientes recibidos a la receta */
    const addIngredientParamsToRecipe = (ingredientParams: Array<CreateRecipeIngredientParamsDTO>) => {
        setRecipeIngredientsParams(ingredientParams);
    }

    /** Elimina un ingrediente de la receta */
    const removeIngredientFromRecipe = (ingredientID: string) => {
        setRecipeIngredientsParams((list) => list.filter(
            (ing) => ing.ingredientID !== ingredientID
        ));
    }

    const addPictureParamsToRecipe = (pictureParams: Array<CreateRecipePictureParamsDTO>) => {
        setRecipePicturesParams(pictureParams);
        console.table(pictureParams);
    }


    const handleSubmit = (e: any) => {
        e.preventDefault();

        let recipe: CreateRecipeParamsDTO = {
            name: name,
            description: description,
            diners: diners,
            duration: duration,
            ingredients: recipeIngredientsParams,
            pictures: recipePicturesParams,
            steps: recipeStepsParams,
            categoryID: categoryID,
            authorID: currentUserID,
        }
        console.log("Current recipe data: ", recipe);
    }


    let ingredientsFormProps: RecipeIngredientsFormProps = {
        ingredientParams: recipeIngredientsParams,
        onAddIngredientParams: addIngredientParamsToRecipe,
        onRemoveIngredientParams: removeIngredientFromRecipe,
    }

    let picturesFormProps: RecipePicturesFormProps = {
        onUploadCallback: addPictureParamsToRecipe,
    }


    return (
        <div>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            <Card bg="light" border="light" style={card}>
                <Card.Header>
                    <h4>
                        <FormattedMessage id="recipes.components.CreateRecipeForm.title" />
                    </h4>
                </Card.Header>

                <Card.Body>
                    <Form
                        ref={(node: HTMLFormElement) => {formRef = node}}
                        onSubmit={(e: FormEvent<HTMLFormElement>) => handleSubmit(e)}
                    >
                        {/* Nombre */}
                        <Row>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.name" /></b>
                                </Form.Label>
                                <Form.Control as="input"
                                    type="text"
                                    value={name}
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => setName(e.target.value)}
                                    placeholder={intl.formatMessage({id: 'recipes.components.CreateRecipeForm.title.placeholder'})}
                                />
                                <Form.Control.Feedback type="invalid" role="alert" >
                                    <FormattedMessage id="common.validation.mandatoryField" />
                                </Form.Control.Feedback>
                            </FormGroup>
                        </Row>

                        {/* Descripción */}
                        <Row>
                            <FormGroup>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.description" /></b>
                                </Form.Label>
                                <Form.Text as="textarea" rows={2} cols={150}
                                    value={description}
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => setDescription(e.target.value)}
                                    placeholder={intl.formatMessage({id: 'recipes.components.CreateRecipeForm.description.placeholder'})}
                                    style={descriptionTextarea}
                                />
                            </FormGroup>
                        </Row>

                        {/* Duración, raciones y selector de categoría */}
                        <Row>
                            <FormGroup as={Col}>
                                <Form.Label>
                                    <b><FormattedMessage id="recipes.components.CreateRecipeForm.duration.title" /></b>
                                </Form.Label>
                                <Form.Control
                                    type="number"
                                    value={duration}
                                    min={0}
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => setDuration(Number(e.target.value))}
                                    placeholder={intl.formatMessage({id: 'recipes.components.CreateRecipeForm.duration.placeholder'})}
                                />
                                <Form.Control.Feedback type="invalid" role="alert" >
                                    <FormattedMessage id="common.validation.mandatoryField" />
                                </Form.Control.Feedback>
                            </FormGroup>

                            <FormGroup as={Col}>
                                <Form.Label>
                                    <b><FormattedMessage id="recipes.components.CreateRecipeForm.diners.title" /></b>
                                </Form.Label>
                                <Form.Control
                                    as="input"
                                    min={0}
                                    type="number"
                                    value={diners}
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => setDiners(Number(e.target.value))}
                                    placeholder={intl.formatMessage({id: 'recipes.components.CreateRecipeForm.diners.placeholder'})}
                                />
                            </FormGroup>

                            <FormGroup as={Col}>
                                <Form.Label>
                                    <b><FormattedMessage id="common.fields.category" /></b>
                                </Form.Label>
                                <CategorySelector onChangeCallback={handleCategorySelectorChange} />
                            </FormGroup>

                        </Row>

                        {/* Ingredientes de la receta */}
                        <Row>
                            <h5>
                                <FormattedMessage id="common.fields.ingredients" />
                            </h5>
                            <RecipeIngredientsForm {...ingredientsFormProps} />
                        </Row>

                        {/* Imágenes de la receta */}
                        <Row>
                            <RecipePicturesForm {...picturesFormProps} />
                        </Row>

                    </Form>
                </Card.Body>

                <Card.Footer>
                    <Button onClick={handleSubmit} >
                        <FormattedMessage id="common.buttons.create" />
                    </Button>
                </Card.Footer>
            </Card>
        </div>
    )
}


export default CreateRecipeForm;
