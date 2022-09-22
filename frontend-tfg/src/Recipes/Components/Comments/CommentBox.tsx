import {Comment} from "../../../Social";
import {Alert, Button, Col, Container, Row} from "react-bootstrap";
import {Link} from "react-router-dom";
import {useAppDispatch, useAppSelector} from "../../../store";
import {userRedux} from "../../../Users";
import {FormattedMessage} from "react-intl";
import React, {MouseEventHandler} from "react";
import {recipesRedux} from "../../Application";
import {ErrorDto} from "../../../App";

interface Props {
    comment: Comment,
    onErrorCallback: (err: ErrorDto) => void,
}

const CommentBox = ({comment, onErrorCallback}: Props) => {
    const dispatch = useAppDispatch();
    const isAdmin = useAppSelector(userRedux.selectors.selectIsAdmin);
    let isBanned: boolean = comment.isBannedByAdmin;

    const handleClick: MouseEventHandler<HTMLButtonElement> = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        let onSuccess: NoArgsCallbackFunction = () => {};
        let onErrors: CallbackFunction = (err) => {

            console.log(err);
            onErrorCallback(err)
        };

        dispatch(recipesRedux.actions.banCommentAsyncAction(comment.id, onSuccess, onErrors))
    }

    return (
        <Container className="my-auto border border-secondary rounded p-2 m-2">
            <Row>
                {/* Información del comentario */}
                <Col md={10}>
                    <Row>
                        {(isBanned) ?
                            <Alert variant="warning">
                                <FormattedMessage id="recipes.warning.CommentIsBannedByAdmin" />
                            </Alert>
                            :
                            <Row>
                                <Row>
                                    <Col md={10}>
                                        <Link to={`/users/${comment.author.userID}`}>
                                            {comment.author.name}
                                        </Link>
                                    </Col>
                                    <Col>
                                        {comment.creationDate.toString()}
                                    </Col>
                                </Row>

                                {/* Texto del comentario */}
                                <Row>
                                    <Col>
                                        {comment.text}
                                    </Col>
                                </Row>
                            </Row>
                        }
                    </Row>
                </Col>

                {/* Botón para banear comentario */}
                {(isAdmin) &&
                    <Col>
                        <Button
                            variant="secondary"
                            onClick={handleClick}
                        >
                            {(isBanned) ?
                                <FormattedMessage id="recipes.components.CommentBox.unbanButton" />
                                :
                                <FormattedMessage id="recipes.components.CommentBox.banButton"/>
                            }
                        </Button>
                    </Col>
                }
            </Row>
        </Container>
    )
}

export default CommentBox;
