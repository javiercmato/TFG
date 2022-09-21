import {Alert, Badge, Button, Card, Col, Row} from "react-bootstrap";
import RecipeSummaryDTO from "../Infrastructure/RecipeSummaryDTO";
import {recipesRedux} from "../index";
import {useAppSelector} from "../../store";
import {FormattedMessage} from "react-intl";
import React from "react";
import {FaClock} from "react-icons/fa";
import {GiKnifeFork} from "react-icons/gi";
import {MdFastfood} from "react-icons/md";
import {Link} from "react-router-dom";
import {userRedux} from "../../Users";

interface Props {
    recipe: RecipeSummaryDTO,
    privateListRemoveButton?: JSX.Element,
}

const RecipeCard = ({recipe, privateListRemoveButton}: Props) => {
    const categories = useAppSelector(recipesRedux.selectors.selectCategories);
    const categoryName = recipesRedux.selectors.selectCategoryName(categories, recipe.categoryID);
    const isAdmin = useAppSelector(userRedux.selectors.selectIsAdmin);
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const isRecipeOwner = (userID === recipe.author.userID);

    return (
        <Card>
            <Card.Header>
                <Badge>
                    {categoryName}
                </Badge>
            </Card.Header>

            {(recipe.picture) &&
                <div className={"w-25"}>
                    <Card.Img src={recipe.picture} />
                </div>
            }

            <Card.Body>
                <Row>
                    <Col>
                        <h5>{recipe.name}</h5>
                    </Col>

                    <Col>
                        <Link to={`/users/${recipe.author.userID}`} >
                            {recipe.author.nickname}
                        </Link>
                    </Col>
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
                                <MdFastfood/> {recipe.ingredientsCount} <FormattedMessage id="common.fields.ingredients"/>
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
                <Row>
                    {(recipe.isBannedByAdmin) &&
                        <Col>
                            <Alert variant="warning">
                                <FormattedMessage id="recipes.warning.RecipeIsBannedByAdmin" />
                            </Alert>
                        </Col>
                    }

                    {// Se pueden ver los detalles si: no está baneada, es el dueño de la receta o es el administrador
                    (!recipe.isBannedByAdmin || isRecipeOwner || isAdmin ) &&
                        <Col>
                            <Link to={`/recipes/${recipe.id}`}>
                                <Button>
                                    <FormattedMessage id="recipes.components.RecipeCard.button.recipeDetails" />
                                </Button>
                            </Link>
                        </Col>
                    }

                    {(privateListRemoveButton) &&
                        <Col>
                            {privateListRemoveButton}
                        </Col>
                    }
                </Row>
            </Card.Footer>
        </Card>
    )
}

export type {Props as RecipeCardProps};
export default RecipeCard;
