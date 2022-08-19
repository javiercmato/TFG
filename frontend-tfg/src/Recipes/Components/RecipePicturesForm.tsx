import {Carousel, Col, Image, Row} from "react-bootstrap";
import {UploadFileButton} from "../../App";
import {useEffect, useState} from "react";
import {CreateRecipePictureParamsDTO} from "../Infrastructure";
import {carouselPicture} from "./styles/recipePicturesForm";

interface Props {
    onUploadCallback: (pictureParams: Array<CreateRecipePictureParamsDTO>) => void;
}

const RecipePicturesForm = ({onUploadCallback}: Props) => {
    const [pictureParams, setPictureParams] = useState<Array<CreateRecipePictureParamsDTO>>([]);
    const [order, setOrder] = useState<number>(0);


    const handleImageUpload = (file: File) => {
        // Convertir imagen a string: https://stackoverflow.com/a/42498790/11295728
        let reader = new FileReader();
        // Callback a ejecutar cuando el fichero se cargue
        reader.onloadend = () => {
            // Crea los parámetros de la imagen y los guarda
            let dataURL = reader.result as string;
            let params: CreateRecipePictureParamsDTO = {
                order: order,
                data: dataURL,
            }
            setPictureParams([...pictureParams, params]);

            // Aumenta el contador para la siguiente foto
            setOrder((o) => o + 1);
        }

        // Cargar fichero
        reader.readAsDataURL(file);
    }


    // Devolver las imágenes al padre cada vez que cambien
    useEffect(() => {
        onUploadCallback(pictureParams);
    }, [pictureParams]);

    return (
        <Col>
            {/* Botón para subir las imágenes */}
            <Row>
                <UploadFileButton
                    fileTipe={"image/*"}
                    onUploadCallback={handleImageUpload}
                />
            </Row>

            {/* Carousel para mostrar las imágenes */}
            <Row>
                <Carousel>
                    {pictureParams.map((picture) =>
                        <Carousel.Item key={picture.order}>
                            <Image thumbnail
                                src={picture.data}
                                style={carouselPicture}
                            />
                        </Carousel.Item>
                    )}
                </Carousel>
            </Row>
        </Col>
    )
};


export type {Props as RecipePicturesFormProps};
export default RecipePicturesForm;
