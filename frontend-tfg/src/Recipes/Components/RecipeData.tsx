import {Recipe} from "../Domain";
import {Badge, Col, Row} from "react-bootstrap";
import {useAppSelector} from "../../store";
import {recipesRedux} from "../Application";
import {FormattedMessage} from "react-intl";
import {FaClock, FaStar} from "react-icons/fa";
import {GiKnifeFork} from "react-icons/gi";
import {userRedux} from "../../Users";
import RateRecipe from "./RateRecipe";

const MAX_RATING_VALUE = Number(process.env.REACT_APP_MAX_RATING_VALUE);

interface Props {
    recipe: Recipe,
    onRateCallback: (value: number) => void,
}

const RecipeData = ({recipe, onRateCallback}: Props) => {
    const categories = useAppSelector(recipesRedux.selectors.selectCategories);
    const categoryName: string = recipesRedux.selectors.selectCategoryName(categories, recipe.categoryID);
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);

    return (
        <Col>
            {/* Categoría*/}
            <Row>
                <Badge>
                    <h6>{categoryName}</h6>
                </Badge>
            </Row>

            {/* Tiempo preparación, raciones y puntuación media*/}
            <Row>
                <Col>
                    <span>
                        <FaClock/> {recipe.duration} <FormattedMessage id="common.fields.minutes"/>
                    </span>
                </Col>

                <Col>
                    <span>
                        <GiKnifeFork/> {recipe.diners} <FormattedMessage id="common.fields.diners"/>
                    </span>
                </Col>
            </Row>

            {/* Puntuación media */}
            <Row>
                <Col>
                    <span>
                        <FaStar /> {recipe.averageRating.toFixed(2)} / {MAX_RATING_VALUE}  <FormattedMessage id="common.fields.averageRating" /> [{recipe.totalVotes}]
                    </span>
                </Col>
            </Row>

            {/* Botones de acciones: puntuar, añadir a lista privada*/}
            {(isLoggedIn) &&
                <Row className="mx-auto">
                    <RateRecipe onRateCallback={onRateCallback} />
                </Row>
            }
        </Col>
    )
}


export type {Props as RecipeDataProps};
export default RecipeData;
