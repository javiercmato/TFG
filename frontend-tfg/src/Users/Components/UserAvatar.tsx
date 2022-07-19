import {Image} from "react-bootstrap";
import Avatar from "react-avatar";

interface Props {
    imageB64?: string,
    userNickname: string,
    isThumbnail?: boolean,
    round?: boolean
}

const UserAvatar = ({imageB64, isThumbnail, userNickname, round}: Props) => {
    if (!imageB64) {
        return (
            <Avatar
                name={userNickname}
                round={round || false}
            />
        )
    }

    return (
        <Image
            src={imageB64}
            thumbnail={isThumbnail || false}
        />
    )
};


export default UserAvatar;
