import React, {MouseEventHandler} from "react";
import {Button} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {useSelector} from "react-redux";
import {recipesRedux} from "../Application";
import {useAppDispatch} from "../../store";
import {Recipe} from "../Domain";
import {userRedux} from "../../Users";

interface Props {
    recipe: Recipe,
    onErrorCallback: CallbackFunction,
}

const BanRecipeButton = ({recipe, onErrorCallback}: Props) => {
    const dispatch = useAppDispatch();
    const isUserAdmin = useSelector(userRedux.selectors.selectIsAdmin);
    let isBanned : boolean = recipe?.isBannedByAdmin;


    const handleClick: MouseEventHandler<HTMLButtonElement> = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        let onSuccess: NoArgsCallbackFunction = () => {};
        let onErrors: CallbackFunction = (err) => {onErrorCallback(err)};

        dispatch(recipesRedux.actions.banRecipeAsyncAction(recipe.id, onSuccess, onErrors));
    }

    // Solo un usuario administrador puede banear usuarios
    if (!isUserAdmin) return null;

    return (
        <Button
            variant="secondary"
            onClick={handleClick}
        >
            {(isBanned) ?
                <FormattedMessage id="recipes.components.BanRecipeButton.unbanButton" />
                :
                <FormattedMessage id="recipes.components.BanRecipeButton.banButton"/>
            }
        </Button>
    )
}

export type {Props as BanRecipeButtonProps};
export default BanRecipeButton;
