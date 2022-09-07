import {FormattedMessage} from "react-intl";
import {Button} from "react-bootstrap";
import {input} from './styles/uploadFileButton';
import {useRef} from "react";

interface Props {
    fileTipe: string,
    onUploadCallback: CallbackFunction,
}

// https://medium.com/codex/use-a-button-to-upload-files-on-your-react-app-with-bootstrap-ef963cbe8280
const UploadFileButton = ({fileTipe, onUploadCallback}: Props) => {
    const inputRef = useRef<HTMLInputElement>(null);                                    // Referencia al input para subir fichero

    const handleSelectFile = () => {
        inputRef.current?.click()
    }

    const handleFileUpload = () => {
        // Si hay ficheros subidos
        if (inputRef.current?.files) {
            let uploadedFile: File = inputRef.current.files[0];
            onUploadCallback(uploadedFile);
        }
    }


    return (
        <div>
            <input
                type="file"
                ref={inputRef}
                onChange={handleFileUpload}
                style={input}
                accept={fileTipe}
            />

            <Button
                onClick={handleSelectFile}
                variant="primary"
            >
                <FormattedMessage id="common.buttons.uploadFile"/>
            </Button>
        </div>
    )
}


export default UploadFileButton;
