import {useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {ErrorDto, Errors} from "../../App";
import {useAppDispatch, useAppSelector} from "../../store";
import {recipesRedux} from "../Application";
import {Alert, Button, Carousel, Col, Container, Image, Row} from "react-bootstrap";
import {
    RecipeData,
    RecipeDataProps,
    RecipeIngredientsList,
    RecipeIngredientsListProps,
    RecipeSteps,
    RecipeStepsProps
} from "./index";
import {userRedux} from "../../Users";
import {FormattedMessage} from "react-intl";
import {carouselPicture} from "./styles/recipePicturesForm";
import {FaTrash} from "react-icons/fa";
import BanRecipeButton, {BanRecipeButtonProps} from "./BanRecipeButton";
import AddToPrivateListButton, {AddToPrivateListButtonProps} from "./AddToPrivateListButton";
import CommentForm from "./Comments/CommentForm";

const RecipeDetails = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const {recipeID} = useParams();
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const recipeData = useAppSelector(recipesRedux.selectors.selectRecipe);
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);
    const isRecipeBanned = useAppSelector(recipesRedux.selectors.isBannedByAdmin);


    const handleDeleteClick = (e: any) => {
        e.preventDefault();

        let onSuccess = () => navigate('/recipes');
        let onError = (error: ErrorDto) => setBackendErrors(error);
        dispatch(recipesRedux.actions.deleteRecipeAsyncAction(String(recipeID), onSuccess, onError));
    }

    const handleBanCommentError = (error: ErrorDto) => setBackendErrors(error);


    let ingredientsListProps: RecipeIngredientsListProps = {
        ingredients: recipeData?.ingredients!,
    }

    let recipeDataProps: RecipeDataProps = {
        recipe: recipeData!,
    }

    let recipeStepsProps: RecipeStepsProps = {
        steps: recipeData?.steps!,
    }

    let banButtonProps: BanRecipeButtonProps = {
        recipe: recipeData!,
        onErrorCallback: setBackendErrors,
    }

    let addToListProps: AddToPrivateListButtonProps = {
        recipe: recipeData!,
        onErrorCallback: handleBanCommentError,
    }


    // Solicita los datos de la receta cada vez que cambie el ID en la URL
    useEffect( () => {
        let onSuccess = () => {};

        let onError = (error: ErrorDto) => {
            setBackendErrors(error);
        }

        dispatch(recipesRedux.actions.getRecipeDetailsAsyncAction(recipeID!, onSuccess, onError));

        // Limpia los datos de la receta buscada cuando se desmonta el componente
        return () => {
            dispatch(recipesRedux.actions.clearRecipeDetailsAction())
        }
    }, [dispatch, recipeID]);


    // Mostrar aviso si no se encuentra la receta
    if (recipeData === null) {
        return (
            <Alert variant="warning">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    // Mostrar aviso si la receta está baneada por el administrador
    if (isRecipeBanned) {
        return (
            <>
                <BanRecipeButton {...banButtonProps} />
                <Alert variant="warning">
                    <FormattedMessage id="recipes.warning.RecipeIsBannedByAdmin" />
                </Alert>
            </>
        )
    }

    return (
        <div>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            <Container>
                {/* Borrar receta */}
                <Row>
                    <Button variant="danger" onClick={handleDeleteClick}>
                        <span>
                            <FaTrash />
                            <FormattedMessage id="common.buttons.delete" />
                        </span>
                    </Button>
                </Row>

                {/* Nombre y descripción*/}
                <Row>
                    <Col>
                        <h2>{recipeData?.name}</h2>
                        <Row>
                            <span className="border border-dark">
                                {recipeData?.description}
                            </span>
                        </Row>
                    </Col>

                    {/* Botones para editar y banear receta */}
                    <Col md={(isLoggedIn) ? 3: 0}>
                        <BanRecipeButton {...banButtonProps} />
                        <AddToPrivateListButton {...addToListProps} />
                    </Col>
                </Row>

                {/* Información básica de la receta */}
                <Row>
                    {/* Ingredientes de la receta */}
                    <Col>
                        <h4>
                            <FormattedMessage id="common.fields.ingredients" />
                        </h4>
                        <RecipeIngredientsList {...ingredientsListProps} />
                    </Col>

                    {/* Categoría, datos, votación y añadir a lista privada */}
                    <Col>
                        <RecipeData {...recipeDataProps} />
                    </Col>
                </Row>

                {/* Pasos de preparación */}
                <Row>
                    <h4>
                        <FormattedMessage id="common.fields.steps" />
                    </h4>
                    <RecipeSteps {...recipeStepsProps} />
                </Row>

                {/* Fotos de la receta */}
                <Row>
                    <h4>
                        <FormattedMessage id="common.fields.pictures" />
                    </h4>
                    {(recipeData.pictures?.length === 0) ?
                        <Alert variant="warning">
                            <FormattedMessage id="common.alerts.noResults" />
                        </Alert>
                        :
                        <Carousel>
                            {recipeData.pictures?.map((picture) =>
                                <Carousel.Item key={picture.order}>
                                    <Image
                                        src={picture.pictureData}
                                        style={carouselPicture}
                                    />
                                </Carousel.Item>
                            )}
                        </Carousel>
                    }

                </Row>

                {/* Comentarios */}
                <Row>
                    <h4>
                        <FormattedMessage id="common.fields.comments" />
                    </h4>
                    <CommentForm comments={recipeData.comments} onErrorCallback={setBackendErrors}/>
                </Row>
            </Container>
        </div>
    )
}


export default RecipeDetails;
