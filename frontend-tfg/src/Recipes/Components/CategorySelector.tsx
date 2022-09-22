import {useAppDispatch, useAppSelector} from "../../store";
import {recipesRedux} from "../Application";
import {Form} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {useEffect} from "react";

interface Props {
    onChangeCallback: any,
}

const CategorySelector = ({onChangeCallback}: Props) => {
    const dispatch = useAppDispatch();
    const categories = useAppSelector(recipesRedux.selectors.selectCategories);

    useEffect(() => {
        let onSuccess = () => {};
        let onError = () => {};
        dispatch(recipesRedux.actions.getCategoriesAsyncAction(onSuccess, onError));
    }, [dispatch]);

    return (
        <Form.Select
            onChange={onChangeCallback}
        >
            {/* Primera opción (índice 0) */}
            <option value={''}>
                <FormattedMessage id={'common.fields.category'} />
            </option>

            {categories.map((c) =>
                <option key={c.id} value={c.id}>
                    {c.name.toUpperCase()}
                </option>
            )}
        </Form.Select>
    )
}


export type {Props as CategorySelectorProps};
export default CategorySelector;
