import {useAppDispatch, useAppSelector} from "../../store";
import {recipesRedux} from "../Application";
import {Alert, Container, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import RecipeSummaryDTO from "../Infrastructure/RecipeSummaryDTO";
import {RecipeCardProps} from "../../Ingredients/Components";
import RecipeCard from "./RecipeCard";
import {Pager, PagerProps, SearchCriteria} from "../../App";
import {container} from './styles/findRecipesResults'

const FindRecipesResults = () => {
    const dispatch = useAppDispatch();
    const searchCriteria = useAppSelector(recipesRedux.selectors.selectSearchCriteria);
    const searchResults = useAppSelector(recipesRedux.selectors.selectSearchResultBlock);

    const handlePreviousPageClick = (event: any) => {
        event.preventDefault();

        let criteria: SearchCriteria = {
            ...searchCriteria,
            page: searchCriteria.page - 1,
        }
        let onSuccess = () => {};
        dispatch(recipesRedux.actions.findRecipesAsyncAction(criteria, onSuccess));
    }

    const handleNextPageClick = (event: any) => {
        event.preventDefault();

        let criteria: SearchCriteria = {
            ...searchCriteria,
            page: searchCriteria.page + 1,
        }
        let onSuccess = () => {};
        dispatch(recipesRedux.actions.findRecipesAsyncAction(criteria, onSuccess));
    }


    let pagerProps: PagerProps = {
        currentPage: searchCriteria.page + 1,
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
        <Container style={container}>
            {/* Tarjetas con los resultados */}
            <Row>
                {searchResults.items.map( (item: RecipeSummaryDTO) => {
                    let props: RecipeCardProps = {
                        recipe: item
                    }

                    return <RecipeCard {...props} key={item.id} />
                })}
            </Row>


            {/* Paginación */}
            <Row>
                <Pager {...pagerProps} />
            </Row>
        </Container>
    )
}

export default FindRecipesResults;
