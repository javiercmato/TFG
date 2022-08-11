import {useAppDispatch} from "../../store";
import {ChangeEvent, FormEvent, useState} from "react";
import {Button, Form, FormControl, InputGroup, Row} from "react-bootstrap";
import {FormattedMessage, useIntl} from "react-intl";
import {rowIngredientsSearch} from "./styles/findIngredients";
import IngredientTypeSelector, {IngredientTypeSelectorProps} from "./IngredientTypeSelector";
import {ingredientsRedux} from "../Application";
import {SearchCriteria} from "../../App";


const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const FindIngredients = () => {
    const intl = useIntl();
    const dispatch = useAppDispatch();
    const [queryName, setQueryName] = useState<string>('');         // Nombre que se usará para la búsqueda
    const [typeIDQuery, setTypeIDQuery] = useState<string>('');     // Tipo de ingrediente que se usará para la búsqueda


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
            page: 0,
            pageSize: DEFAULT_PAGE_SIZE,
            name: (queryName !== '') ? queryName : null,
            type: (typeIDQuery !== '') ? typeIDQuery : null,
        }

        let onSuccess = () => {};
        // Distinguir si hay una búsqueda por criterios o si se buscan todos los ingredientes
        let hasCriteria = (criteria.type === null) && (criteria.name === null);
        let action = (hasCriteria) ?
            ingredientsRedux.actions.findAllIngredientsAsyncAction(criteria, onSuccess) :
            ingredientsRedux.actions.findIngredientsAsyncAction(criteria, onSuccess);
        dispatch(action);
    }

    let typeSelectorProps: IngredientTypeSelectorProps = {
        onChangeCallback: handleChangeType
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

                            <IngredientTypeSelector {...typeSelectorProps}/>

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
