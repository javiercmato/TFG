import {useAppDispatch, useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";
import {FormattedMessage} from "react-intl";
import {Alert, Col, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import IngredientItem from "./IngredientItem";
import {pagerRow, resultItem, resultsRow, titleRow} from './styles/findIngredientsResults';
import {Block, Pager, PagerProps, SearchCriteria} from "../../App";
import {Ingredient} from "../Domain";


const FindIngredientsResults = () => {
    const dispatch = useAppDispatch();
    const searchCriteria = useAppSelector(ingredientsRedux.selectors.selectSearchCriteria);
    const searchResults = useAppSelector(ingredientsRedux.selectors.selectSearchResultBlock);

    const handlePreviousPageClick = (event: any) => {
        event.preventDefault();

        console.log('PREVIOUS CLICKED');
        let criteria: SearchCriteria = {
            ...searchCriteria,
            page: searchCriteria.page - 1
        }
        let onSuccess = (block: Block<Ingredient>) => {
            console.log('PREVIOUS RESULTS:');
            console.table(block.items)
        };
        // Distinguir si hay una búsqueda por criterios o si se buscan todos los ingredientes
        let hasCriteria = (criteria.type === null) || (criteria.name === null);
        let action = (hasCriteria) ?
            ingredientsRedux.actions.findAllIngredientsAsyncAction(criteria, onSuccess) :
            ingredientsRedux.actions.findIngredientsAsyncAction(criteria, onSuccess);
        dispatch(action);
    }

    const handleNextPageClick = (event: any) => {
        event.preventDefault();

        console.log('NEXT CLICKED');
        let criteria: SearchCriteria = {
            ...searchCriteria,
            page: searchCriteria.page + 1
        }
        let onSuccess = (block: Block<Ingredient>) => {
            console.log('NEXT RESULTS:');
            console.table(block.items)
        };
        // Distinguir si hay una búsqueda por criterios o si se buscan todos los ingredientes
        let hasCriteria = (criteria.type === null) || (criteria.name === null);
        let action = (hasCriteria) ?
            ingredientsRedux.actions.findAllIngredientsAsyncAction(criteria, onSuccess) :
            ingredientsRedux.actions.findIngredientsAsyncAction(criteria, onSuccess);
        dispatch(action);
    }

    let pagerProps: PagerProps = {
        currentPage: searchCriteria.page,
        previous: {
            enabled: searchCriteria.page > 0,
            onClickCallback: handlePreviousPageClick
        },
        next: {
            enabled: searchResults?.hasMoreItems!,
            onClickCallback: handleNextPageClick
        }
    }



    if (searchResults === null || searchResults.itemsCount === 0) {
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
                    {searchResults.items.map( (item) =>
                        <ListGroupItem key={item.id} style={resultItem}>
                            <IngredientItem ingredient={item} />
                        </ListGroupItem>
                    )}
                </ListGroup>
            </Row>

            <Row style={pagerRow}>
                <Col>
                    <div className={"d-flex justify-content-center"}>
                        <Pager {...pagerProps} />
                    </div>
                </Col>
            </Row>
        </Col>
    )
}

export default FindIngredientsResults;
