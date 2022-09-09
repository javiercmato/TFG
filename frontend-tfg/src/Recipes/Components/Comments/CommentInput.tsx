import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {ChangeEvent, useState} from "react";
import {FormattedMessage, useIntl} from "react-intl";
import {useParams} from "react-router-dom";
import {useAppDispatch, useAppSelector} from "../../../store";
import {ErrorDto} from "../../../App";
import {recipesRedux} from "../../Application";
import {CreateCommentParamsDTO} from "../../Infrastructure";
import {userRedux} from "../../../Users";

interface Props {
    onErrorCallback: any,
}

const CommentInput = ({onErrorCallback}: Props) => {
    const intl = useIntl();
    const dispatch = useAppDispatch();
    const {recipeID} = useParams();
    const [text, setText] = useState<string>('');
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);
    const userID = useAppSelector(userRedux.selectors.selectUserID);

    const handlePublishClick = () => {
        let params: CreateCommentParamsDTO = {
            userID: userID,
            text: text
        }

        let onSuccess = () => {};
        let onError = (error: ErrorDto) => onErrorCallback(error);

        dispatch(recipesRedux.actions.addCommentAsyncAction(recipeID!, params, onSuccess, onErrorCallback))
    }

    // Si usuario no está logeado no puede añadir comentarios
    if (!isLoggedIn) return null;

    return (
        <Container>
            <Row>
                <Col md={10}>
                    <Form.Control
                        as="textarea"
                        rows={2}
                        cols={50}
                        value={text}
                        onChange={(e: ChangeEvent<HTMLInputElement>) => setText(e.target.value)}
                        placeholder={intl.formatMessage({id: 'recipes.components.CommentInput.placeholder'})}
                    />
                </Col>

                <Col sm={2}>
                    <Button
                        disabled={text === ''}
                        onClick={handlePublishClick}
                    >
                        <FormattedMessage id="common.buttons.publish" />
                    </Button>
                </Col>
            </Row>
        </Container>
    )
}

export default CommentInput;
