import {Alert, Modal} from "react-bootstrap";
import {FormattedMessage} from "react-intl";

interface Props {
    show: boolean,
    setShow: any,
    formattedMessageID: string
}

const WarningModal = ({show, setShow, formattedMessageID}: Props) => {

    return (
        <Modal
            show={show}
            onHide={() => setShow(false)}
        >
            <Modal.Header closeButton>
                <FormattedMessage id="common.Components.WarningModal.title" />
            </Modal.Header>

            <Modal.Body>
                <Alert variant="warning">
                    <FormattedMessage id={formattedMessageID} />
                </Alert>
            </Modal.Body>
        </Modal>
    )
}

export type {Props as WarningModalProps};
export default WarningModal;
