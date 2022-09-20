import CreateCategory from "./CreateCategory";
import {Button, Col, Row} from "react-bootstrap";
import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../../Users";
import {useNavigate} from "react-router-dom";
import {FormattedMessage} from "react-intl";
import FindRecipesForm from "./FindRecipesForm";
import {FindRecipesResults} from "./index";
import {useEffect, useState} from "react";
import {WarningModal, WarningModalProps} from "../../App";
import {recipesRedux} from "../Application";

const DEFAULT_PAGE_SIZE: number = Number(process.env.REACT_APP_DEFAULT_PAGE_SIZE);

const RecipesPage = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const isAdminLoggedIn = useAppSelector(userRedux.selectors.selectIsAdmin);
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);
    const [showModal, setShowModal] = useState<boolean>(false);

    const handleCreateRecipeClick = (e: any) => {
        e.preventDefault();

        (isLoggedIn) ? navigate('/recipes/create') : setShowModal(true);
    }

    let modalProps: WarningModalProps = {
        show: showModal,
        setShow: setShowModal,
        formattedMessageID: "common.alerts.notLoggedIn",
    }

    useEffect( () => {
        let onSuccess: CallbackFunction = () => {};
        let onError: CallbackFunction = () => {};
        dispatch(recipesRedux.actions.getCategoriesAsyncAction(onSuccess, onError))
    })

    return (
        <>
            <Row>
                {/* Formulario para crear y mostrar las categorías */}
                <Col md={2}>
                    {(isAdminLoggedIn) &&
                        <Row className={"gy-3"}>
                            <CreateCategory />
                        </Row>
                    }

                    <Row>
                        <Button onClick={handleCreateRecipeClick}>
                            <FormattedMessage id={'recipes.components.CreateRecipeButton.title'} />
                        </Button>
                    </Row>
                </Col>

                {/* Buscador de recetas y resultados */}
                <Col>
                    <FindRecipesForm />
                    <FindRecipesResults />
                </Col>
            </Row>

            <WarningModal {...modalProps} />
        </>
    );
}

export default RecipesPage;
