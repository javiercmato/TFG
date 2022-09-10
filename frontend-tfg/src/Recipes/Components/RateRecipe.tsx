import {useState} from "react";
// @ts-ignore
import ReactStars from "react-rating-stars-component";

const MAX_RATING_VALUE: number = Number(process.env.REACT_APP_MAX_RATING_VALUE);

interface Props {
    onRateCallback: (value: number) => void,
}

const RateRecipe = ({onRateCallback}: Props) => {
    const [rate, setRate] = useState<number>(0);


    const handleRateChange = (newRate: number) => {
        setRate(newRate);

        onRateCallback(newRate);
    }

    return (
        <ReactStars
            count={MAX_RATING_VALUE}
            value={rate}
            onChange={handleRateChange}
            size={30}
            isHalf={true}
            emptyIcon={<i className="far fa-star"></i>}
            halfIcon={<i className="fa fa-star-half-alt"></i>}
            fullIcon={<i className="fa fa-star"></i>}
            activeColor="#ffd700"
        />
    )
}

export default RateRecipe;
export type {Props as RateRecipeProps};
