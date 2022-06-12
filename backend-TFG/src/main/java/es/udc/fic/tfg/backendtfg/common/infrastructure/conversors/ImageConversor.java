package es.udc.fic.tfg.backendtfg.common.infrastructure.conversors;

public interface ImageConversor {
    /**
     * Codifica los bytes de una imagen a un String en Base64
     * @param imageBytes Bytes de la imagen
     * @return String en Base64
     */
    public String encodeToBase64String(byte[] imageBytes);
    
    /**
     * Codifica una imagen en Base64 a una array de bytes.
     * @param imageBase64String String de la imagen codificada en Base64
     * @return Bytes de la imagen
     */
    public byte[] encodeToByteArray(String imageBase64String);
}
