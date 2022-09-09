import {Comment} from "../../../Social";
import {Col, Container, Row} from "react-bootstrap";
import {Link} from "react-router-dom";

interface Props {
    comment: Comment,
}

const CommentBox = ({comment}: Props) => {

    return (
        <Container className="my-auto border border-secondary">
            {/* Información del comentario */}
            <Row>
                <Col md={9}>
                    <Link to={`/users/${comment.author.nickname}`}>
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
        </Container>
    )
}

export default CommentBox;
