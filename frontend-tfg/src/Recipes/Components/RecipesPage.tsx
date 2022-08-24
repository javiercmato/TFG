import CreateCategory from "./CreateCategory";
import {Button, Col, Row} from "react-bootstrap";
import {useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {useNavigate} from "react-router-dom";
import {FormattedMessage} from "react-intl";
import FindRecipesForm from "./FindRecipesForm";
import {FindRecipesResults} from "./index";

const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const RecipesPage = () => {
    const navigate = useNavigate();
    const isAdminLoggedIn = useAppSelector(userRedux.selectors.selectIsAdmin);



    return (
        <Row>
            {/* Formulario para crear y mostrar las categorías */}
            {(isAdminLoggedIn) &&
                <Col md={4}>
                    <Row className={"gy-3"}>
                        <CreateCategory />
                    </Row>

                    <Row>
                        <Button onClick={() => navigate('/recipes/create')}>
                            <FormattedMessage id={'recipes.components.CreateRecipeButton.title'} />
                        </Button>
                    </Row>
                </Col>
            }

            {/* Buscador de recetas y resultados */}
            <Col>
                <FindRecipesForm />
                <FindRecipesResults />
            </Col>
        </Row>
    )
}

export default RecipesPage;
