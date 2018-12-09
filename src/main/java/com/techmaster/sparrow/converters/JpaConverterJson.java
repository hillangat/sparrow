package com.techmaster.sparrow.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

// @Converter(autoApply = true)
public class JpaConverterJson implements AttributeConverter<Object, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(JpaConverterJson.class);

    @Override
    public String convertToDatabaseColumn(Object meta) {
        try {
            return meta == null ? null : objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            logger.error("Unexpected IOEx decoding json from database: \n" + meta.toString());
            return null;
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, Object.class);
        } catch (IOException ex) {
            logger.error("Unexpected IOEx decoding json from database: \n" + dbData);
            return null;
        }
    }

}
