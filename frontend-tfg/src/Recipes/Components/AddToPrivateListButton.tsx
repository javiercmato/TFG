import {Recipe} from "../Domain";
import {useAppDispatch, useAppSelector} from "../../store";
import React, {useState} from "react";
import {PrivateListSelector, userRedux} from "../../Users";
import {Alert, Button, Modal} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {PrivateListSelectorProps} from "../../Users/Components/PrivateListSelector";

interface Props {
    recipe: Recipe,
    onErrorCallback: CallbackFunction,
}

const AddToPrivateListButton = ({recipe, onErrorCallback}: Props) => {
    const dispatch = useAppDispatch();
    const userID = useAppSelector(userRedux.selectors.selectUserID);
    const isLoggedIn = useAppSelector(userRedux.selectors.isLoggedIn);
    const [listID, setListId] = useState<string>('');
    const [show, setShow] = useState<boolean>(false);


    const handleModalAddButton = (e: any) => {
        e.preventDefault();

        let onSuccess: NoArgsCallbackFunction = () => {
            setShow(false);
        };
        let onErrors: CallbackFunction = (err) => {onErrorCallback(err)};
        dispatch(userRedux.actions.addRecipeToPrivateListAsyncAction(userID, listID, recipe.id, onSuccess, onErrors));
    }

    const handlePrivateListChange = (e: any) => {
        let id: string = e.target.value;
        setListId(id);
    }

    let privateListSelectorProps: PrivateListSelectorProps = {
        onChangeCallback: handlePrivateListChange,
    }

    return (
        <>
            <Button onClick={() => setShow(true)}>
                <FormattedMessage id="recipes.components.AddToPrivateListButton.text" />
            </Button>

            <Modal
                show={show}
                onHide={() => setShow(false)}
            >
                <Modal.Header closeButton />
                <Modal.Body>
                    {(isLoggedIn) ?

                        <PrivateListSelector {...privateListSelectorProps} />
                    :
                        <Alert variant="danger">
                            <FormattedMessage id="common.alerts.notLoggedIn" />
                        </Alert>
                    }
                </Modal.Body>

                <Modal.Footer>
                    <Button onClick={handleModalAddButton}>
                        <FormattedMessage id="common.buttons.add" />
                    </Button>
                </Modal.Footer>

            </Modal>
        </>
    );
}

export type {Props as AddToPrivateListButtonProps};
export default AddToPrivateListButton;
