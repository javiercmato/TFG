import {IngredientType} from "../Domain";
import {ListGroupItem} from "react-bootstrap";

interface Props {
    item: IngredientType,
    index: number,
    isActive: boolean,
    onClickCallback: any,
}

const IngredientTypesListItem = ({item, isActive, index, onClickCallback}: Props) => {
    const handleClick = (e: any) => {
        onClickCallback(e, index);
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
