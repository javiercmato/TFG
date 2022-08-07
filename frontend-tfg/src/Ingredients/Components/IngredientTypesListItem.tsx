import {IngredientType} from "../Domain";
import {ListGroupItem} from "react-bootstrap";
import {useNavigate} from "react-router-dom";

interface Props {
    item: IngredientType,
    index: number,
    isActive: boolean,
    onClickCallback: any,
}

const IngredientTypesListItem = ({item, isActive, index, onClickCallback}: Props) => {
    const navigate = useNavigate();

    const handleClick = (e: any) => {
        onClickCallback(e, index);

        navigate(`/ingredients/${item.id}`);
    }

    return (
        <ListGroupItem
            active={isActive}
            action
            as="button"
            onClick={handleClick}
        >
            {item.name.toUpperCase()}
        </ListGroupItem>
    )
}


export default IngredientTypesListItem;
