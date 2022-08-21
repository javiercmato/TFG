import {Recipe} from "../Domain";
import {Badge, Col, Row} from "react-bootstrap";
import {useAppSelector} from "../../store";
import {recipesRedux} from "../Application";
import {FormattedMessage} from "react-intl";
import {FaClock} from "react-icons/fa";
import {GiKnifeFork} from "react-icons/gi";
import {userRedux} from "../../Users";

interface Props {
    recipe: Recipe
}

const RecipeData = ({recipe}: Props) => {
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
            <Row></Row>

            {/* Botones de acciones: puntuar, añadir a lista privada*/}
            {(isLoggedIn) &&
                <Row></Row>
            }
        </Col>
    )
}


export type {Props as RecipeDataProps};
export default RecipeData;
