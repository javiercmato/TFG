import {Comment} from "../../../Social";
import {Alert, Container, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import CommentInput from "./CommentInput";
import CommentBox from "./CommentBox";
import {ErrorDto} from "../../../App";
import {commentsForm} from "../styles/recipeDetails";


interface Props {
    comments: Array<Comment>,
    onErrorCallback: (error: ErrorDto) => void,
}

const CommentForm = ({comments, onErrorCallback}: Props) => {
    const hasComments = (comments.length !== 0);


    return (
        <Container style={commentsForm}>
            <Row>
                <CommentInput onErrorCallback={onErrorCallback}/>
            </Row>

            <br />

            <Row>
                {(hasComments) ?
                    comments.map((comment: Comment, index: number) =>
                        <CommentBox key={index}
                                    comment={comment}
                                    onErrorCallback={onErrorCallback}
                        />
                    )
                    :
                    <Alert variant="info">
                        <FormattedMessage id="common.alerts.noResults" />
                    </Alert>
                }
            </Row>
        </Container>
    )
}

export default CommentForm;
