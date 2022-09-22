import {Link, useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {ErrorDto, Errors} from "../../App";
import {useAppDispatch, useAppSelector} from "../../store";
import {recipesRedux} from "../Application";
import {Alert, Button, Carousel, Col, Container, Row} from "react-bootstrap";
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
import {FaTrash} from "react-icons/fa";
import BanRecipeButton, {BanRecipeButtonProps} from "./BanRecipeButton";
import AddToPrivateListButton, {AddToPrivateListButtonProps} from "./AddToPrivateListButton";
import CommentForm from "./Comments/CommentForm";
import {RateRecipeParamsDTO} from "../Infrastructure";
import Picture from "./Picture";
import {RecipePicture} from "../Domain";
import {container, recipeButtons, row} from './styles/recipeDetails';

const RecipeDetails = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const {recipeID} = useParams();
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const recipeData = useAppSelector(recipesRedux.selectors.selectRecipe);
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const isRecipeBanned = useAppSelector(recipesRedux.selectors.isBannedByAdmin);

    const isRecipeOwner = (isLoggedIn && recipeData?.author.userID === userID);

    const handleDeleteClick = (e: any) => {
        e.preventDefault();

        let onSuccess = () => {
            navigate('/recipes')
        };
        let onError = (error: ErrorDto) => setBackendErrors(error);
        dispatch(recipesRedux.actions.deleteRecipeAsyncAction(String(recipeID), onSuccess, onError));
    }

    const handleError = (error: ErrorDto) => setBackendErrors(error);

    const handleRateRecipe = (value: number) => {
        let rateParams: RateRecipeParamsDTO = {
            rating: value,
            userID: userID,
        }

        let onSuccess = () => {console.log("Receta puntuada")};
        let onError = (error: ErrorDto) => setBackendErrors(error);
        dispatch(recipesRedux.actions.rateRecipeAsyncAction(String(recipeID), rateParams, onSuccess, onError));
    }


    let ingredientsListProps: RecipeIngredientsListProps = {
        ingredients: recipeData?.ingredients!,
    }

    let recipeDataProps: RecipeDataProps = {
        recipe: recipeData!,
        onRateCallback: handleRateRecipe,
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
        onErrorCallback: handleError,
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
            <Row>
                <Col md={9}>
                    <Alert variant="warning">
                        <FormattedMessage id="recipes.warning.RecipeIsBannedByAdmin" />
                    </Alert>
                </Col>

                <Col>
                    <BanRecipeButton {...banButtonProps} />
                </Col>
            </Row>
        )
    }

    return (
        <div>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            <Container style={container}>
                {/* Borrar receta */}
                {(isRecipeOwner) &&
                    <Row style={row}>
                        <Button variant="danger" onClick={handleDeleteClick}>
                            <span>
                                <FaTrash />
                                <FormattedMessage id="common.buttons.delete" />
                            </span>
                        </Button>
                    </Row>
                }

                {/* Nombre y descripción*/}
                <Row style={row}>
                    <Col>
                        <Row style={row}>
                            <Col md={10}>
                                <h2>{recipeData?.name}</h2>
                            </Col>
                            <Col md={2}>
                                <Link to={`/users/${recipeData.author.userID}`}>
                                    {recipeData.author.nickname}
                                </Link>
                            </Col>
                        </Row>

                        <br />

                        <Row style={row}>
                            <span className="border border-dark">
                                {recipeData?.description}
                            </span>
                        </Row>
                    </Col>

                    {/* Botones para añadir a lista privada y banear receta */}
                    <Col md={(isLoggedIn) ? 3: 0} style={recipeButtons}>
                        <BanRecipeButton {...banButtonProps} />
                        <AddToPrivateListButton {...addToListProps} />
                    </Col>
                </Row>

                {/* Información básica de la receta */}
                <Row style={row}>
                    {/* Ingredientes de la receta */}
                    <Col>
                        <h4>
                            <FormattedMessage id="common.fields.ingredients" />
                        </h4>
                        <RecipeIngredientsList {...ingredientsListProps} />
                    </Col>

                    {/* Categoría, datos, votación y añadir a lista privada */}
                    <Col>
                        <Row style={row}>
                            <RecipeData {...recipeDataProps}/>
                        </Row>

                    </Col>
                </Row>

                <br/>

                {/* Pasos de preparación */}
                <Row style={row}>
                    <h4>
                        <FormattedMessage id="common.fields.steps" />
                    </h4>
                    <RecipeSteps {...recipeStepsProps} />
                </Row>

                <br />

                {/* Fotos de la receta */}
                <Row style={row}>
                    <h4>
                        <FormattedMessage id="common.fields.pictures" />
                    </h4>
                    {(recipeData.pictures?.length === 0) ?
                        <Alert variant="warning">
                            <FormattedMessage id="common.alerts.noResults" />
                        </Alert>
                        :
                        <Carousel>
                            {recipeData.pictures?.map((picture: RecipePicture) =>
                                <Carousel.Item key={picture.order}>
                                    <div style={{minHeight: "400px", minWidth: "400px"}}>
                                        <Picture b64String={picture.pictureData} bigSize={true} />
                                    </div>
                                </Carousel.Item>
                            )}
                        </Carousel>
                    }
                </Row>

                <br />

                {/* Comentarios */}
                <Row style={row}>
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
