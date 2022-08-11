import {Ingredient} from "../Domain";
import {Badge} from "react-bootstrap";
import {name} from "./styles/ingredientItem";

interface Props {
    ingredient: Ingredient
}

const IngredientItem = ({ingredient}: Props) => {
    const capitalizedName = ingredient.name.charAt(0).toUpperCase() + ingredient.name.substring(1);

    return (
        <Badge pill bg="light" text="muted">
            <span style={name}>
                {capitalizedName}
            </span>
        </Badge>
    )
}


export default IngredientItem;
