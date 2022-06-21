import {useIntl} from "react-intl";
import {ErrorDto, FieldErrorDto} from "../Domain";
import {Alert, Button, Fade} from "react-bootstrap";
import {FaTimes} from "react-icons/fa";


interface Props {
    /** Error a mostrar */
    error: Nullable<ErrorDto>,
    /** Callback a ejecutar al cerrar el componente */
    onCloseCallback: NoArgsCallbackFunction,
}

const Errors = ({error, onCloseCallback}: Props) => {
    const intl = useIntl();
    let globalError: string = '';                // Error global
    let fieldErrors: FieldErrorDto[] = [];       // Lista de errores en campos del formulario

    // Si no hay error, no devuelve nada
    if (!error) return null;

    // Extraer datos del error recibido
    if (error.globalError) {
        globalError = error.globalError;
    } else if (error.fieldErrors) {
        fieldErrors = [];
        // Para cada campo, extrae el campo que produce el error y el mensaje asociado
        error.fieldErrors.forEach( (err: FieldErrorDto) => {
            let fieldName = intl.formatMessage({
                id: `common.fieldName.${err.fieldname}`
            });
            fieldErrors.push({
                fieldname: fieldName,
                message: err.message
            });
        });
    }

    return (
        <Alert
            variant="danger"
            dismissible={true}
            transition={Fade}
            show={true}
        >
            <Alert.Heading>{globalError}</Alert.Heading>
            {fieldErrors.map( (field: FieldErrorDto, index: number) =>
                <li key={index}>
                    <b>{field.fieldname}</b> : {field.message}
                </li>
            )}

            <Button
                type="button"
                onClick={onCloseCallback}
            >
                <FaTimes />
            </Button>
        </Alert>
    )

};


export default Errors;
