package com.outcast.rpgclass.repositroy;

import com.outcast.rpgclass.api.stat.AttributeType;
import jakarta.persistence.AttributeConverter;

public class AttributeTypeConverter implements AttributeConverter<AttributeType, String> {

    @Override
    public String convertToDatabaseColumn(AttributeType attribute) {
        return attribute.getId();
    }

    @Override
    public AttributeType convertToEntityAttribute(String dbData) {
        System.out.println("dbData string : " + dbData);
        return null;
    }

}
