import {UserSummaryDTO} from "../../Users";

interface Follow {
    following: UserSummaryDTO,
    followed: UserSummaryDTO,
    followDate: Date,
}

export default Follow;
