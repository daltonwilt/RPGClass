package com.outcast.rpgclass.util.description;

import com.outcast.rpgclass.RPGClass;
import com.outcast.rpgclass.api.skill.DescriptionArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

public class ExpressionDescription implements DescriptionArgument {

    protected String expression;

    public ExpressionDescription(String expression) {
        this.expression = expression;
    }

    @Override
    public TextComponent apply(LivingEntity living) {
        return Component.text(RPGClass.getInstance().getExpressionService().evalExpression(living, expression).doubleValue());
    }

}
