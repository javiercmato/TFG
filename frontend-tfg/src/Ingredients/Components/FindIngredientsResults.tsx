import {useAppDispatch, useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";
import {FormattedMessage} from "react-intl";
import {Alert, Col, Row} from "react-bootstrap";
import IngredientItem from "./IngredientItem";
import {pagerRow, resultsRow, titleRow} from './styles/findIngredientsResults';


const FindIngredientsResults = () => {
    const dispatch = useAppDispatch();
    const ingredientSearch = useAppSelector(ingredientsRedux.selectors.getIngredientSearch);
    const searchCriteria = useAppSelector(ingredientsRedux.selectors.selectSearchCriteria);
    const searchResults = useAppSelector(ingredientsRedux.selectors.selectSearchResultBlock);


    if (ingredientSearch.result === null) {
        return (
            <Alert variant="info">
                <FormattedMessage id="common.alerts.noResults" />
            </Alert>
        )
    }

    return (
        <Col>
            <Row style={titleRow}>
                <h3><FormattedMessage id="common.results" /></h3>
            </Row>

            <Row style={resultsRow}>
                {ingredientSearch.result!.items.map((ing) =>
                    <Col xs={6}>
                        <IngredientItem ingredient={ing} />
                    </Col>
                )}
            </Row>

            <Row style={pagerRow}>
                <Col>
                </Col>
            </Row>
        </Col>
    )
}

export default FindIngredientsResults;
