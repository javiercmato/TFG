import {useAppDispatch, useAppSelector} from "../../store";
import {useEffect} from "react";
import {recipesRedux} from "../../Recipes";
import defaultSearchCriteria, {SearchCriteria} from "../../App/Domain/common/SearchCriteria";
import {container} from "../../Recipes/Components/styles/findRecipesResults";
import {Alert, Col, Container, Row} from "react-bootstrap";
import RecipeSummaryDTO from "../../Recipes/Infrastructure/RecipeSummaryDTO";
import RecipeCard, {RecipeCardProps} from "../../Recipes/Components/RecipeCard";
import {Pager, PagerProps} from "../../App";
import {FormattedMessage} from "react-intl";


interface Props {
    userID: string,
}

const UserRecipes = ({userID}: Props) => {
    const dispatch = useAppDispatch();
    const searchCriteria = useAppSelector(recipesRedux.selectors.selectSearchCriteria);
    const searchResults = useAppSelector(recipesRedux.selectors.selectSearchResultBlock);

    const handlePreviousPageClick = (event: any) => {
        event.preventDefault();

        let criteria: SearchCriteria = {
            ...searchCriteria,
            page: searchCriteria.page - 1,
            userID: userID
        }
        let onSuccess = () => {};
        dispatch(recipesRedux.actions.findRecipesAsyncAction(criteria, onSuccess));
    }

    const handleNextPageClick = (event: any) => {
        event.preventDefault();

        let criteria: SearchCriteria = {
            ...searchCriteria,
            page: searchCriteria.page + 1,
            userID: userID

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

    // Buscar las recetas al cargar el componente
    useEffect( () => {
        let criteria: SearchCriteria = {
            ...defaultSearchCriteria,
            userID: userID,
        }
        let onSuccess = () => {};
        dispatch(recipesRedux.actions.findRecipesByAuthorAsyncAction(criteria, onSuccess));

        // Limpiar resultados al desmontar componente
        return () => {
            dispatch(recipesRedux.actions.clearRecipesSearchAction())
        };
    }, [userID])


    if (searchResults?.itemsCount === 0) {
        return (
            <Alert variant="info">
                <FormattedMessage id="users.warning.UserHasNoRecipes" />
            </Alert>
        )
    }

    return (
        <div>
            <Container style={container}>
                {/* Tarjetas con los resultados */}
                <Row>
                    {searchResults?.items.map( (item: RecipeSummaryDTO) => {
                        let props: RecipeCardProps = {
                            recipe: item
                        }

                        return <RecipeCard {...props} key={item.id} />
                    })}
                </Row>

                <br />

                {/* Paginación */}
                <Row>
                    <Col>
                        <div className={"d-flex justify-content-center"}>
                            <Pager {...pagerProps} />
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export type {Props as UserRecipesProps};
export default UserRecipes;
