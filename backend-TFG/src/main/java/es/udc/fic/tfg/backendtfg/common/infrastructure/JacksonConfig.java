package es.udc.fic.tfg.backendtfg.common.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

import java.time.LocalDateTime;

@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    public ObjectMapper configureObjectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new JacksonLocalDateTimeSerializer());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
