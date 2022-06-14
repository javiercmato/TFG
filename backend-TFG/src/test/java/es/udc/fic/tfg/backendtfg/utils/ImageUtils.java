package es.udc.fic.tfg.backendtfg.utils;

import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Utilidad para cargar imágenes en los tests
 * https://mkyong.com/java/how-to-convert-bufferedimage-to-byte-in-java/
 */
@UtilityClass
public class ImageUtils {
    public static final String TEST_ASSETS_PATH = "src/test/assets/";
    public static final String PNG_EXTENSION = "png";
    
    /** Carga una imagen y la devuelve como byte[] */
    public static byte[] loadImageFromResourceName(String resourceName, String imageExtension) {
        Path resourcePath = Paths.get(TEST_ASSETS_PATH, resourceName);
        File resourceFile = resourcePath.toFile();
        byte[] imageBytes;
        
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
            BufferedImage bi = ImageIO.read(resourceFile);
            ImageIO.write(bi, imageExtension, baos);
            imageBytes = baos.toByteArray();
        } catch ( Exception ex) {
            imageBytes = new byte[0];
        }
        
        return imageBytes;
    }
}
