import {FormattedMessage, useIntl} from "react-intl";
import {Button, Modal, ModalBody, ModalDialog, ModalFooter, ModalHeader, ModalTitle} from "react-bootstrap";
import {AppErrorType} from "../Application/state/actionTypes";

interface Props {
    /** Error a mostrar */
    error: AppErrorType,
    /** Callback a ejecutar cuando se cierra el diálogo */
    onCloseCallback: NoArgsCallbackFunction,
}


const ErrorDialog = ({error, onCloseCallback} : Props) => {
    const intl = useIntl();

    const errorMessage: string = (error! instanceof Error) ?
        // Si error es una excepción, traduce el mensaje
        intl.formatMessage({id: `proxy.exceptions.${error.name}`})
        // Sino, error es un ErrorDTO
        : error!.globalError;

    return (
        <Modal>
            <ModalDialog>
                <ModalHeader>
                    <ModalTitle>
                        <FormattedMessage id='app.components.ErrorDialog.title' />
                    </ModalTitle>
                </ModalHeader>

                <ModalBody>
                    <p>{errorMessage}</p>
                </ModalBody>

                <ModalFooter>
                    <Button variant='primary'
                        onClick={onCloseCallback}
                    >
                        <FormattedMessage id='common.buttons.close' />
                    </Button>
                </ModalFooter>
            </ModalDialog>
        </Modal>
    )

}


export default ErrorDialog;
