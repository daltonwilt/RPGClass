package com.outcast.rpgclass.api.skill;

import com.outcast.rpgskill.api.skill.TargetedCastable;
import org.bukkit.entity.LivingEntity;

//===========================================================================================================
// Abstraction that will be implemented when you want to create a targeted skill
//
// Doesn't currently work because raycasting doesn't work
//===========================================================================================================
public abstract class TargetedSkill extends Skill implements TargetedCastable {

    public static final String MAX_RANGE_PROPERTY = "max-range";

    protected TargetedSkill(SkillSpec skillSpec) {
        super(skillSpec);
    }

    @Override
    public double getRange(LivingEntity living) {
        return asDouble(living, getProperty(MAX_RANGE_PROPERTY, String.class, "100.0"));
    }

}
