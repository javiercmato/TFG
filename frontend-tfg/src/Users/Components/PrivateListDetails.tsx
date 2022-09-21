import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../Application";
import {ErrorDto, Errors} from "../../App";
import {Alert, Button, Card} from "react-bootstrap";
import {RecipeSummaryDTO} from "../../Recipes";
import {card, cardHeader, cardSubtitle} from './styles/privateListDetails';
import {FormattedMessage} from "react-intl";
import RecipeCard, {RecipeCardProps} from "../../Recipes/Components/RecipeCard";

interface Props {
    listID: string,
}

const PrivateListDetails = ({listID}: Props) => {
    const dispatch = useAppDispatch();
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const [backendErrors, setBackendErrors] = useState<Nullable<ErrorDto>>(null);
    const listDetails = useAppSelector(userRedux.selectors.getPrivateListDetailsModule);
    const hasRecipes: boolean = listDetails?.recipes.length !== 0;

    const handleRemoveRecipeFromListClick = (recipeID: string): any => {
        let onSuccess: NoArgsCallbackFunction = () => {};
        let onError = (error: ErrorDto) => {setBackendErrors(error)};
        dispatch(userRedux.actions.removeRecipeFromPrivateListAsyncAction(userID, String(listID), recipeID, onSuccess, onError));
    }

    const handleDeleteListClick = (): any => {
        let onSuccess: NoArgsCallbackFunction = () => {};
        let onError = (error: ErrorDto) => {setBackendErrors(error)};
        dispatch(userRedux.actions.deletePrivateListAsyncAction(userID, String(listID), onSuccess, onError));
    }


    useEffect(() => {
        let onSuccess: CallbackFunction = () => {};
        let onError = (error: ErrorDto) => {setBackendErrors(error);}
        dispatch(userRedux.actions.getPrivateListDetailsAsyncAction(userID, String(listID), onSuccess, onError));
    }, [listID])

    return (
        <div>
            <Errors
                error={backendErrors}
                onCloseCallback={() => setBackendErrors(null)}
            />

            <Card style={card}>
                <Card.Header style={cardHeader}>
                    <h4>
                        {listDetails?.title}
                    </h4>
                </Card.Header>

                <Card.Subtitle style={cardSubtitle}>
                    <h5>
                        {listDetails?.description}
                    </h5>
                </Card.Subtitle>

                <Card.Body>
                    {(hasRecipes) ?
                            listDetails?.recipes.map((recipe: RecipeSummaryDTO) => {
                                let removeFromListButton =
                                    <Button
                                        variant="outline-danger"
                                        onClick={() => handleRemoveRecipeFromListClick(recipe.id)}
                                    >
                                        <FormattedMessage id="recipes.components.RecipeCard.button.removeFromList" />
                                    </Button>

                                let recipeCardProps: RecipeCardProps = {
                                    recipe: recipe,
                                    privateListRemoveButton: removeFromListButton,
                                }

                                return <RecipeCard key={recipe.id} {...recipeCardProps} />
                            })
                        :
                        <Alert variant="warning">
                            <FormattedMessage id="common.alerts.listWithoutItems" />
                        </Alert>
                    }
                </Card.Body>

                <Card.Footer>
                    <Button
                        variant="danger"
                        onClick={() => handleDeleteListClick()}
                    >
                        <FormattedMessage id="common.buttons.delete" />
                    </Button>
                </Card.Footer>
            </Card>
        </div>
    )
}

export type {Props as PrivateListDetailsProps};
export default PrivateListDetails;
