import {useAppDispatch, useAppSelector} from "../../store";
import {userRedux} from "../Application";
import {Form} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {useEffect} from "react";

interface Props {
    onChangeCallback: any,
}

const PrivateListSelector = ({onChangeCallback}: Props) => {
    const dispatch = useAppDispatch();
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const privateLists = useAppSelector(userRedux.selectors.selectPrivateLists);

    useEffect(() =>{
        let onSuccess = () => {};
        let onError = () => {};
        dispatch(userRedux.actions.getPrivateListsAsyncAction(userID, onSuccess, onError));
    }, [userID])

    return (
        <Form.Select onChange={onChangeCallback}>
            {/* Primera opción (índice 0) */}
            <option value={''}>
                <FormattedMessage id={'common.fields.privateLists'} />
            </option>

            {privateLists.map((pl) =>
                <option key={pl.id} value={pl.id}>
                    {pl.title}
                </option>
            )}
        </Form.Select>
    )
}

export type {Props as PrivateListSelectorProps};
export default PrivateListSelector;
