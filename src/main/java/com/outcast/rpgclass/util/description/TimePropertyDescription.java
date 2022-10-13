package com.outcast.rpgclass.util.description;

import com.outcast.rpgclass.api.skill.Skill;
import com.outcast.rpgcore.util.TextUtil;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

public class TimePropertyDescription extends PropertyDescription {

    public TimePropertyDescription(Skill skill, String propertyName, String defaultValue) {
        super(skill, propertyName, defaultValue);
    }

    @Override
    public TextComponent apply(LivingEntity living) {
        return TextUtil.formatDuration(asLong(living));
    }

}
