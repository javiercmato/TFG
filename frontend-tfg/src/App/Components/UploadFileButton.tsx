import {FormattedMessage} from "react-intl";
import {Button} from "react-bootstrap";
import {input} from './styles/uploadFileButton';
import {useRef, useState} from "react";

interface Props {
    fileTipe: string,
    onUploadCallback: CallbackFunction,
}

// https://medium.com/codex/use-a-button-to-upload-files-on-your-react-app-with-bootstrap-ef963cbe8280
const UploadFileButton = ({fileTipe, onUploadCallback}: Props) => {
    const inputRef = useRef<HTMLInputElement>(null);                                    // Referencia al input para subir fichero
    const [inputFileName, setInputFileName] = useState<Nullable<string>>(null);         // Nombre del fichero subido

    const handleSelectFile = () => {
        inputRef.current?.click()
    }

    const handleFileUpload = () => {
        // Si hay ficheros subidos
        if (inputRef.current?.files) {
            let uploadedFile: File = inputRef.current.files[0];
            setInputFileName(uploadedFile.name);       // Asigna nombre del fichero al botón
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
                variant={(inputFileName) ? "success" : "primary"}
            >
                {(inputFileName) ?? <FormattedMessage id="common.buttons.uploadFile"/>}
            </Button>
        </div>
    )
}


export default UploadFileButton;
