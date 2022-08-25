import {Badge, Button, Card, Col, Row} from "react-bootstrap";
import RecipeSummaryDTO from "../../Recipes/Infrastructure/RecipeSummaryDTO";
import {recipesRedux} from "../../Recipes";
import {useAppSelector} from "../../store";
import {FormattedMessage} from "react-intl";
import React from "react";
import {FaClock} from "react-icons/fa";
import {GiKnifeFork} from "react-icons/gi";
import {MdFastfood} from "react-icons/md";
import {Link} from "react-router-dom";

interface Props {
    recipe: RecipeSummaryDTO
}

const RecipeCard = ({recipe}: Props) => {
    const categories = useAppSelector(recipesRedux.selectors.selectCategories);
    const categoryName = recipesRedux.selectors.selectCategoryName(categories, recipe.categoryID);


    return (
        <Card>
            <Card.Header>
                <Badge>
                    {categoryName}
                </Badge>
            </Card.Header>

            {(recipe.picture) &&
                <Card.Img src={recipe.picture} />
            }

            <Card.Body>
                <Row>
                    <h5>{recipe.name}</h5>
                </Row>

                <Row>
                    <span>{recipe.description}</span>
                </Row>

                <Row>
                    <Col>
                        <Row>
                            <span>
                                <FaClock/> {recipe.duration} <FormattedMessage id="common.fields.minutes"/>
                            </span>
                        </Row>

                        <Row>
                            <span>
                                <MdFastfood/> {recipe.duration} <FormattedMessage id="common.fields.minutes"/>
                            </span>
                        </Row>
                    </Col>

                    <Col>
                        <Row>
                            <span>
                                <GiKnifeFork/> {recipe.diners} <FormattedMessage id="common.fields.diners"/>
                            </span>
                        </Row>
                    </Col>
                </Row>
            </Card.Body>

            <Card.Footer>
                <Link to={`/recipes/${recipe.id}`}>
                    <Button>
                        <FormattedMessage id="recipes.components.RecipeCard.button" />
                    </Button>
                </Link>
            </Card.Footer>
        </Card>
    )
}

export type {Props as RecipeCardProps};
export default RecipeCard;
