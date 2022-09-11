import {useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";
import {Container, Form, ListGroup, ListGroupItem} from "react-bootstrap";
import {ChangeEvent, useEffect} from "react";


interface Props {
    ingredientIDList: Array<string>,
    setIngredientIDList: any,
    onCheckItemCallback: (ingredientsIDList: Array<string>) => void,
}

const IngredientsChecklist = ({ingredientIDList, setIngredientIDList, onCheckItemCallback}: Props) => {
    const ingredientsSearch = useAppSelector(ingredientsRedux.selectors.selectSearchResultBlock);

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        let isChecked = e.target.checked;
        let ingredientID = e.target.value;
        let checkedItems: Array<string> = [...ingredientIDList];

        if (isChecked) {
            if (!checkedItems.includes(ingredientID)) {
                checkedItems.push(ingredientID);
           }
        } else {
            checkedItems = ingredientIDList.filter((id: string) => id !== ingredientID)
        }

        // Envía los elementos marcados al padre
        setIngredientIDList(checkedItems);
    }

    // Devuelve los ID de los ingredientes selecionados al padre cada vez que se produzca un cambio
    useEffect(() => {
        onCheckItemCallback(ingredientIDList)
        }, [ingredientIDList]
    )

    if (ingredientsSearch === null || ingredientsSearch.itemsCount === 0) {
        return null;
    }

    return (
        <Container>
            <ListGroup>
                {ingredientsSearch.items.map( (item) =>
                    <ListGroupItem key={item.id}>
                        <Form.Check
                            label={item.name}
                            value={item.id}
                            onChange={handleChange}
                        />
                    </ListGroupItem>
                )}
            </ListGroup>
        </Container>
    )
}


export type {Props as IngredientsChecklistProps};
export default IngredientsChecklist;
