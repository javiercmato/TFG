import {UserSummaryDTO} from "../../Users";

interface Comment {
    id: string,
    author: UserSummaryDTO,
    creationDate: Date,
    text: string,
    isBannedByAdmin: boolean,
}

export default Comment;
