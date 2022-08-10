import {useAppDispatch, useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";
import {FormattedMessage} from "react-intl";
import {Alert, Col, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import IngredientItem from "./IngredientItem";
import {pagerRow, resultItem, resultsRow, titleRow} from './styles/findIngredientsResults';


const FindIngredientsResults = () => {
    const dispatch = useAppDispatch();
    const ingredientSearch = useAppSelector(ingredientsRedux.selectors.getIngredientSearch);
    const searchCriteria = useAppSelector(ingredientsRedux.selectors.selectSearchCriteria);
    const searchResults = useAppSelector(ingredientsRedux.selectors.selectSearchResultBlock);


    if (searchResults === null) {
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
                <ListGroup>
                    {searchResults!.items.map( (item) =>
                        <ListGroupItem key={item.id} style={resultItem}>
                            <IngredientItem ingredient={item} />
                        </ListGroupItem>
                    )}
                </ListGroup>
            </Row>

            <Row style={pagerRow}>
                <Col>
                </Col>
            </Row>
        </Col>
    )
}

export default FindIngredientsResults;
