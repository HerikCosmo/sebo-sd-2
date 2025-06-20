package org.example.rmi.protocol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonMarshaller {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static byte[] marshal(Object obj)  throws IOException {
        return objectMapper.writeValueAsBytes(obj);
    }

    public static <T> T unmarshal(byte[] data, Class<T> specificClass) throws IOException {
        return objectMapper.readValue(data, specificClass);
    }

    public static <T> T unmarshal(byte[] bytes, TypeReference<T> typeReference)  throws IOException {
        return objectMapper.readValue(bytes, typeReference);
    }
}
