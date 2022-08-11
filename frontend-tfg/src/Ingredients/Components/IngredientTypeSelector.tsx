import {FormattedMessage} from "react-intl";
import {Form} from "react-bootstrap";
import {useSelector} from "react-redux";
import {ingredientsRedux} from "../Application";

interface Props {
    onChangeCallback: any,
}

const IngredientTypeSelector = ({onChangeCallback} : Props) => {
    const ingredientTypes = useSelector(ingredientsRedux.selectors.selectIngrediendtTypes);

    return (
        <Form.Select
            onChange={onChangeCallback}
        >
            {/* Primera opción (índice 0) */}
            <option
                value={''}
            >
                <FormattedMessage id={'common.fields.ingredientType'} />
            </option>

            {ingredientTypes.map((t) =>
                <option
                    key={t.id}
                    value={t.id}
                >
                    {t.name.toUpperCase()}
                </option>
            )}
        </Form.Select>
    )
}

export type {Props as IngredientTypeSelectorProps};
export default IngredientTypeSelector;
