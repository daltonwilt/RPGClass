package com.outcast.rpgclass.util.description;

import com.outcast.rpgclass.api.skill.Skill;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

public class SpeedPropertyDescription extends PropertyDescription {

    public SpeedPropertyDescription(Skill skill, String propertyName, String defaultValue) {
        super(skill, propertyName, defaultValue);
    }

    @Override
    public TextComponent apply(LivingEntity living) {
        double value = evalExpression(living).doubleValue();
        // 0 == Speed 1, for each level of speed, movement speed is increased by 20%
        value = (value + 1) * 20;
        return Component.text(String.format("%.2f", value) + "%");
    }

}
