import {UserSummaryDTO} from "../../Users";

interface Comment {
    author: UserSummaryDTO,
    creationDate: Date,
    text: string,
}

export default Comment;
