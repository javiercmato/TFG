import {useAppDispatch} from "../../store";
import {ChangeEvent, FormEvent, useState} from "react";
import {Button, Form, FormControl, InputGroup, Row} from "react-bootstrap";
import {FormattedMessage, useIntl} from "react-intl";
import {rowIngredientsSearch} from "./styles/findIngredients";
import IngredientTypeSelector from "./IngredientTypeSelector";
import {ingredientsRedux} from "../Application";
import {SearchCriteria} from "../../App";


const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const FindIngredients = () => {
    const intl = useIntl();
    const dispatch = useAppDispatch();
    const [queryName, setQueryName] = useState<string>('');         // Nombre que se usará para la búsqueda
    const [typeIDQuery, setTypeIDQuery] = useState<any>('');     // Tipo de ingrediente que se usará para la búsqueda


    const handleChangeType = (e: any) => {
        e.preventDefault();

        setTypeIDQuery(e.target.value);
    }

    const handleClearButtonClick = (e: any) => {
        e.preventDefault();

        setQueryName('');
        setTypeIDQuery('');
        dispatch(ingredientsRedux.actions.clearIngredientsSearchAction());
    }

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let criteria: SearchCriteria = {
            name: queryName,
            type: typeIDQuery,
            page: 0,
            pageSize: DEFAULT_PAGE_SIZE
        }

        let onSuccess = () => {};
        // Si hay tipo seleccionado, se busca por nombre y tipo. Sino solo por nombre
        let action = (criteria.type) ?
            ingredientsRedux.actions.findIngredientsByNameAndTypeAsyncAction(criteria, onSuccess)
            : ingredientsRedux.actions.findIngredientsByNameAsyncAction(criteria, onSuccess);
        dispatch(action);
    }

    return (
        <div>
            <Row style={rowIngredientsSearch}>
                <h4><FormattedMessage id="ingredients.components.FindIngredients.findIngredients" /></h4>
                <br/>
                <Form
                    onSubmit={handleSubmit}
                >
                    <Row>
                        <InputGroup>
                            <FormControl
                                as="input"
                                type="text"
                                value={queryName}
                                placeholder={intl.formatMessage({id: 'ingredients.components.CreateIngredient.placeholder'})}
                                onChange={(e: ChangeEvent<HTMLInputElement>) => setQueryName(e.target.value)}
                            />

                            <IngredientTypeSelector onChangeCallback={handleChangeType}/>

                            <Button type="submit">
                                <FormattedMessage id="common.buttons.search" />
                            </Button>
                            <Button variant="dark"
                                onClick={handleClearButtonClick}
                            >
                                <FormattedMessage id="common.buttons.clear" />
                            </Button>
                        </InputGroup>
                    </Row>
                </Form>
            </Row>
        </div>
    )
}

export default FindIngredients;
