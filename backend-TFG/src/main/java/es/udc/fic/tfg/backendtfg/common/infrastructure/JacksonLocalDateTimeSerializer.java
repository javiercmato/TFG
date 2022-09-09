package es.udc.fic.tfg.backendtfg.common.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JacksonLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {
    private static final long serialVersionUID = 1355852411036457107L;
    
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    
    public JacksonLocalDateTimeSerializer() {
        this(null);
    }
    
    protected JacksonLocalDateTimeSerializer(Class<LocalDateTime> type) {
        super(type);
    }
    
    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(formatter.format(value));
    }
}
