package com.outcast.rpgclass.repositroy;

import com.outcast.rpgclass.RPGClass;
import com.outcast.rpgclass.character.Role;
import jakarta.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.getName();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return RPGClass.getInstance().getCharacterService().getRole(dbData).orElse(Role.adventurer);
    }

}
