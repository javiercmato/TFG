import {useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {ErrorDto, Errors} from "../../App";
import {useAppDispatch, useAppSelector} from "../../store";
import {recipesRedux} from "../Application";
import {Col, Container, FormText, Row} from "react-bootstrap";
import {RecipeIngredientsList, RecipeIngredientsListProps} from "./index";

const RecipeDetails = () => {
    const dispatch = useAppDispatch();
    const {recipeID} = useParams();
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const recipeData = useAppSelector(recipesRedux.selectors.selectRecipe);

    let ingredientsListProps: RecipeIngredientsListProps = {
        ingredients: recipeData?.ingredients!,
    }

    // Solicita los datos de la receta cada vez que cambie el ID en la URL
    useEffect( () => {
        let onSuccess = () => {};

        let onError = (error: ErrorDto) => {
            setBackendErrors(error);
        }

        dispatch(recipesRedux.actions.getRecipeDetailsAsyncAction(recipeID!, onSuccess, onError));
    }, [recipeID]);

    return (
        <div>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            <Container>
                <Col>
                   {/* Información básica de la receta */}
                   <Row>
                       <Col md={8}>
                           {/* Nombre y descripción*/}
                           <Row>
                               <h2>{recipeData?.name}</h2>
                               <FormText className="border border-dark">
                                   {recipeData?.description}
                               </FormText>
                           </Row>

                           {/* Ingredientes */}
                           <Row>
                               <RecipeIngredientsList {...ingredientsListProps} />
                           </Row>
                       </Col>

                       {/* Categoría, datos, votación y añadir a lista privada */}
                       <Col md={4}>

                       </Col>
                   </Row>

                   {/* Pasos de preparación */}
                   <Row>
                       PASOS
                   </Row>

                   {/* Fotos de la receta */}
                   <Row>
                       FOTOS
                   </Row>

                   {/* Comentarios */}
                   <Row>
                       COMENTARIOS
                   </Row>

                </Col>
            </Container>
        </div>
    )
}


export default RecipeDetails;
