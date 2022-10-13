package com.outcast.rpgclass.data;

import com.outcast.rpgclass.api.stat.AttributeType;

import java.util.HashMap;
import java.util.Map;

public class AttributeData {

    private Map<AttributeType, Double> attributes = new HashMap<>();

    AttributeData(Map<AttributeType, Double> attributes) {
        this.attributes = attributes;
    }

    public Map<AttributeType, Double> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<AttributeType, Double> attributes) {
        this.attributes = attributes;
    }



}
