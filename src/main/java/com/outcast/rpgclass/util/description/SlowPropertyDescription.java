package com.outcast.rpgclass.util.description;

import com.outcast.rpgclass.api.skill.Skill;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

public class SlowPropertyDescription extends PropertyDescription {

    public SlowPropertyDescription(Skill skill, String propertyName, String defaultValue) {
        super(skill, propertyName, defaultValue);
    }

    @Override
    public TextComponent apply(LivingEntity living) {
        double value = evalExpression(living).doubleValue();
        // 0 == Slowness 1, for each level of slowness, movement speed is decreased by 15%
        value = (value + 1) * 15;
        return Component.text(String.format("%.2f", value) + "%");
    }

}
