import {CSSProperties} from "react";

const basic: CSSProperties = {
    margin: "10px",
}

export const picture: CSSProperties = {
    ...basic,
    maxWidth: "200px",
    maxHeight: "200px",
}

export const pictureXL: CSSProperties = {
    ...basic,
    maxWidth: "500px",
    maxHeight: "500px",
}
