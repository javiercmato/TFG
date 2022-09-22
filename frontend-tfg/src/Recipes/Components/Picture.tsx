import {Image} from "react-bootstrap";
import {picture, pictureXL} from './styles/picture';

interface Props {
    b64String: string,
    bigSize: boolean
}

const Picture = ({b64String, bigSize = false}: Props) => {

    return (
        <div>
            <Image loading={"lazy"}
                src={b64String}
                style={(bigSize) ? pictureXL : picture}
            />
        </div>
    )
}

export default Picture;
