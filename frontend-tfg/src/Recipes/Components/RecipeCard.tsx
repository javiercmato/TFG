import {Alert, Badge, Button, Card, Col, Container, Row} from "react-bootstrap";
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
import Picture from "./Picture";
import {card, cardFooter, cardHeader, category, row} from './styles/recipeCard';

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
        <Card style={card}>
            <Card.Header style={cardHeader}>
                <Row>
                    <Badge style={category}>
                        {categoryName}
                    </Badge>
                </Row>
            </Card.Header>

            {(recipe.picture) &&
                <Picture b64String={recipe.picture} bigSize={false}/>
            }

            <Card.Body>
                <Row style={row}>
                    <Col>
                        <h5>{recipe.name}</h5>
                    </Col>

                    <Col>
                        <Link to={`/users/${recipe.author.userID}`} >
                            {recipe.author.nickname}
                        </Link>
                    </Col>
                </Row>

                <Row style={row}>
                    <span>{recipe.description}</span>
                </Row>

                <Row style={row}>
                    <Col>
                        <Row style={row}>
                            <span>
                                <FaClock/> {recipe.duration} <FormattedMessage id="common.fields.minutes"/>
                            </span>
                        </Row>

                        <Row style={row}>
                            <span>
                                <MdFastfood/> {recipe.ingredientsCount} <FormattedMessage id="common.fields.ingredients"/>
                            </span>
                        </Row>
                    </Col>

                    <Col>
                        <Row style={row}>
                            <span>
                                <GiKnifeFork/> {recipe.diners} <FormattedMessage id="common.fields.diners"/>
                            </span>
                        </Row>
                    </Col>
                </Row>
            </Card.Body>

            <Card.Footer style={cardFooter}>
                <Container>
                    <Row style={row}>
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
                </Container>
            </Card.Footer>
        </Card>
    )
}

export type {Props as RecipeCardProps};
export default RecipeCard;
