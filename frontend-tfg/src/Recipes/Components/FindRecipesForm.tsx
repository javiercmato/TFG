import {Button, Col, Container, FormControl, FormLabel, Row} from "react-bootstrap";
import {ChangeEvent, useEffect, useState} from "react";
import {FormattedMessage, useIntl} from "react-intl";
import CategorySelector from "./CategorySelector";
import FindIngredients from "../../Ingredients/Components/FindIngredients";
import {IngredientsChecklist, IngredientsChecklistProps} from "../../Ingredients";
import {defaultSearchCriteria, SearchCriteria} from "../../App";
import {useAppDispatch} from "../../store";
import {recipesRedux} from "../Application";
import {searchButton} from "./styles/findRecipesForm";


const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const FindRecipesForm = () => {
    const intl = useIntl();
    const dispatch = useAppDispatch();
    const [nameQuery, setNameQuery] = useState<string>('');
    const [categoryIDQuery, setCategoryIDQuery] = useState<string>('');
    const [ingredientIDListQuery, setIngredientIDListQuery] = useState<Array<string>>([]);

    const handleCategoryChange = (e:any) => {
        e.preventDefault();

        setCategoryIDQuery(e.target.value);
    }

    const addIngredientsToQuery = (ingredientsIDList: Array<string>) : void => {
        setIngredientIDListQuery(ingredientsIDList);
    }

    const handleSubmit = (e: any) => {
        e.preventDefault();

        let criteria: SearchCriteria = {
            ...defaultSearchCriteria,
            page: 0,
            pageSize: DEFAULT_PAGE_SIZE,
            name: (nameQuery !== '') ? nameQuery : null,
            category: (categoryIDQuery !== '') ? categoryIDQuery : null,
            ingredients: (ingredientIDListQuery.length > 0) ? ingredientIDListQuery: null,
        }

        let onSuccess = () => {};
        dispatch(recipesRedux.actions.findRecipesAsyncAction(criteria, onSuccess));
    }

    let ingredientsChecklistProps: IngredientsChecklistProps = {
        ingredientIDList: ingredientIDListQuery,
        setIngredientIDList: setIngredientIDListQuery,
        onCheckItemCallback: addIngredientsToQuery,
    }


    useEffect(() => {

        // Libera del store la búsqueda realizada
        return () => {
            dispatch(recipesRedux.actions.clearRecipesSearchAction());
        };
    }, )


    return (
        <Container>
            <Col>
                <Row>
                    <FormLabel>
                        <FormattedMessage id="common.fields.name" />
                    </FormLabel>
                    <FormControl
                        as="input"
                        type="text"
                        value={nameQuery ?? ''}
                        placeholder={intl.formatMessage({id: 'recipes.components.CreateRecipeForm.title.placeholder'})}
                        onChange={(e: ChangeEvent<HTMLInputElement>) => setNameQuery(e.target.value)}
                    />
                </Row>

                <Row>
                    <CategorySelector onChangeCallback={handleCategoryChange} />
                </Row>

                <Row>
                    <FindIngredients />
                    <IngredientsChecklist {...ingredientsChecklistProps} />
                </Row>

                <Row>
                    <Button onClick={handleSubmit} style={searchButton}>
                        <FormattedMessage id="recipes.components.FindRecipesForm.searchButton" />
                    </Button>
                </Row>
            </Col>
        </Container>
    )
}


export default FindRecipesForm;
