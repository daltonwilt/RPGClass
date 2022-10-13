package com.outcast.rpgclass.util.description;

import com.outcast.rpgclass.RPGClass;
import com.outcast.rpgclass.api.skill.DescriptionArgument;
import com.outcast.rpgclass.api.skill.Skill;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.udojava.evalex.Expression;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

import java.math.BigDecimal;
import java.util.Map;

public class PropertyDescription implements DescriptionArgument {

    protected Skill skill;
    protected String propertyName;
    protected String defaultValue;

    public PropertyDescription(Skill skill, String propertyName, String defaultValue) {
        this.skill = skill;
        this.propertyName = propertyName;
        this.defaultValue = defaultValue;
    }

    @Override
    public TextComponent apply(LivingEntity living) {
        double value = evalExpression(living).doubleValue();
        return Component.text(String.format("%.2f", value));
    }

    public BigDecimal evalExpression(LivingEntity source) {
        String expressionString = skill.getProperty(propertyName, String.class, defaultValue);
        Expression expression = RPGClass.getInstance().getExpressionService().getExpression(expressionString);

        // Fill in TARGET attributes so the expression can still be viewed in descriptions
        Map<AttributeType, Double> defaultAttributes = RPGClass.getInstance().getAttributeService().getDefaultAttributes();
        RPGClass.getInstance().getExpressionService().populateTargetAttributes(expression, defaultAttributes);

        return RPGClass.getInstance().getExpressionService().evalExpression(source, expression);
    }

    public long asLong(LivingEntity living) {
        String expression = skill.getProperty(propertyName, String.class, defaultValue);
        return RPGClass.getInstance().getExpressionService().evalExpression(living, expression).longValue();
    }

}
