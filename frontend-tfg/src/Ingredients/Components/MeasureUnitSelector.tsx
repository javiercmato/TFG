import {useAppSelector} from "../../store";
import {ingredientsRedux} from "../Application";
import {Form} from "react-bootstrap";
import {FormattedMessage} from "react-intl";

interface Props {
    onChangeCallback: any,
}

const MeasureUnitSelector = ({onChangeCallback} : Props) => {
    const units = useAppSelector(ingredientsRedux.selectors.selectMeasureUnits);

    return (
        <Form.Select onChange={onChangeCallback}>
            {/* Primera opción */}
            <option value={''}>
                <FormattedMessage id={'common.fields.measureUnit'} />
            </option>

            {units.map((u: string, index: number) =>
                <option key={index} value={u}>
                    {u.toUpperCase()}
                </option>
            )}
        </Form.Select>
    )
}


export type {Props as MeasureUnitSelectorProps};
export default MeasureUnitSelector;
