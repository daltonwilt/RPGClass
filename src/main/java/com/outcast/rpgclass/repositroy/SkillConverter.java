package com.outcast.rpgclass.repositroy;

import com.outcast.rpgclass.api.skill.BlankSkill;
import com.outcast.rpgclass.api.skill.Skill;
import com.outcast.rpgskill.RPGSkill;
import jakarta.persistence.AttributeConverter;

public class SkillConverter implements AttributeConverter<Skill, String> {

    @Override
    public String convertToDatabaseColumn(Skill skill) {
        return skill.getId();
    }

    @Override
    public Skill convertToEntityAttribute(String skillId) {
        return (Skill) RPGSkill.getInstance().getSkillService().getSkillById(skillId).orElse(BlankSkill.blank);
    }

}
