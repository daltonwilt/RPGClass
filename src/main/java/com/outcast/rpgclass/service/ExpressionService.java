package com.outcast.rpgclass.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgclass.api.stat.AttributeTypeRegistry;
import com.outcast.rpgclass.config.stat.AttributesConfig;
import com.outcast.rpgclass.util.ExpressionUtil;
import com.udojava.evalex.Expression;
import org.bukkit.entity.LivingEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ExpressionService {

    @Inject
    private AttributeService attributeService;

    @Inject
    private AttributeTypeRegistry attributeTypeRegistry;

    @Inject
    private AttributesConfig config;

    private Map<String, Expression> cachedExpressions = new HashMap<>();

    // Map for attribute variable strings: first is SOURCE, second is TARGET
    private Map<AttributeType, Map.Entry<String, String>> attributeVariables;

    public void init() {
        this.attributeVariables = new HashMap<>();

        attributeTypeRegistry.getAll().forEach( type -> {
            String source = "SOURCE_" + type.getShortName().toUpperCase();
            String target = "TARGET_" + type.getShortName().toUpperCase();

            attributeVariables.put(type, Map.entry(source, target));
        });
    }

    public Expression getExpression(String expression) {
        Expression result = cachedExpressions.get(expression);

        if (result == null) {
            result = new Expression(expression);
            result.addFunction(new ExpressionUtil());
            cachedExpressions.put(expression, result);
        }

        return result;
    }

    public void populateAttributes(Expression expression, Map<AttributeType, Double> attributes, String name) {
        String pattern = name.toUpperCase() + "_%s";
        attributes.forEach((type, value) -> expression.setVariable(
                String.format(pattern, type.getShortName().toUpperCase()),
                BigDecimal.valueOf(value)
        ));
    }

    public void populateSourceAttributes(Expression expression, Map<AttributeType, Double> attributes) {
        attributes.forEach((type, value) -> expression.setVariable(
                attributeVariables.get(type).getKey(),
                BigDecimal.valueOf(value)
        ));
    }

    public void populateTargetAttributes(Expression expression, Map<AttributeType, Double> attributes) {
        attributes.forEach((type, value) -> expression.setVariable(
                attributeVariables.get(type).getValue(),
                BigDecimal.valueOf(value)
        ));
    }

    public BigDecimal evalExpression(LivingEntity source, String stringExpression) {
        return evalExpression(source, getExpression(stringExpression));
    }

    public BigDecimal evalExpression(LivingEntity source, LivingEntity target, String stringExpression) {
        return evalExpression(source, target, getExpression(stringExpression));
    }

    public BigDecimal evalExpression(LivingEntity source, Expression expression) {
        populateSourceAttributes(expression, attributeService.getAllAttributes(source));

        return expression.eval(true);
    }

    public BigDecimal evalExpression(LivingEntity source, LivingEntity target, Expression expression) {
        populateSourceAttributes(expression, attributeService.getAllAttributes(source));
        populateTargetAttributes(expression, attributeService.getAllAttributes(target));

        return expression.eval(true);
    }

}
