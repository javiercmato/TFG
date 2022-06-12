package es.udc.fic.tfg.backendtfg.common.infrastructure.conversors;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ImageConversorImpl implements ImageConversor {
    
    @Override
    public String encodeToBase64String(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
    
    @Override
    public byte[] encodeToByteArray(String imageBase64String) {
        return Base64.getDecoder().decode(imageBase64String);
    }
}
