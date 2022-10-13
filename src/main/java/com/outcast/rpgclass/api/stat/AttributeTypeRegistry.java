package com.outcast.rpgclass.api.stat;

import com.outcast.rpgclass.config.stat.AttributesConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

//===========================================================================================================
// Class that stores those different attribute types in a map so they can be later retrieved for
// manipulation or calculation
//===========================================================================================================
public class AttributeTypeRegistry {

    private Map<String, AttributeType> attributeTypeMap = new LinkedHashMap<>();

    public AttributeTypeRegistry () {
        try {
            AttributesConfig config = new AttributesConfig();
            config.init();

            config.ATTRIBUTE_TYPES.stream().map(conf -> new AttributeType(
                    conf.getId(),
                    conf.getShortName(),
                    conf.getName(),
                    conf.getDescription(),
                    conf.isUpgradable(),
                    conf.isHidden(),
                    conf.getColor(),
                    conf.getDefaultValue(),
                    conf.isResetOnLogin(),
                    conf.getDisplay()
            )).forEach(type -> attributeTypeMap.put(type.getId(), type));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<AttributeType> getById(String id) {
        return Optional.ofNullable(attributeTypeMap.get(id));
    }

    public Collection<AttributeType> getAll() {
        return attributeTypeMap.values();
    }

}
