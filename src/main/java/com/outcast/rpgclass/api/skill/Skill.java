package com.outcast.rpgclass.api.skill;

import com.outcast.rpgclass.RPGClass;
import com.outcast.rpgclass.util.ConversionUtils;
import com.outcast.rpgskill.api.skill.Castable;
import com.udojava.evalex.Expression;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.Objects;

//===========================================================================================================
// Abstract skill that implements castable to use the cast functionality of RPGSkills and stores skill data
//===========================================================================================================
public abstract class Skill implements Castable {

    private String id;
    private String name;
    private String permission;
    private TextComponent descriptionTemplate;
    private Map.Entry<String, ?>[] descriptionArguments;
    private String cooldownExpression;
    private String resourceCostExpression;
    private Map<String, String> properties;

    protected Skill(SkillSpec skillSpec) {
        this.id = skillSpec.getId();
        this.name = skillSpec.getName();
        this.permission = skillSpec.getPermission();
        this.descriptionTemplate = skillSpec.getDescriptionTemplate();
        this.descriptionArguments = skillSpec.getDescriptionArguments();
        this.cooldownExpression = skillSpec.getCooldownExpression();
        this.resourceCostExpression = skillSpec.getResourceCostExpression();
        this.properties = skillSpec.getProperties();
    }

    //===========================================================================================================
    // Utility methods for evaluating an expression inside given string
    //===========================================================================================================
    protected static Expression asExpression(String expression) {
        return RPGClass.getInstance().getExpressionService().getExpression(expression);
    }

    protected static double asDouble(LivingEntity source, String exp) {
        return RPGClass.getInstance().getExpressionService().evalExpression(source, exp).doubleValue();
    }

    protected static double asDouble(LivingEntity source, LivingEntity target, String exp) {
        return RPGClass.getInstance().getExpressionService().evalExpression(source, target, exp).doubleValue();
    }

    protected static int asInt(LivingEntity source, String exp) {
        return RPGClass.getInstance().getExpressionService().evalExpression(source, exp).intValue();
    }

    protected static int asInt(LivingEntity source, LivingEntity target, String exp) {
        return RPGClass.getInstance().getExpressionService().evalExpression(source, target, exp).intValue();
    }

    //===========================================================================================================
    // Getters/Setters for skill data
    //===========================================================================================================
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return getProperty("name", String.class, name);
    }

    @Override
    public TextComponent getDescription(LivingEntity living) {
        return RPGClass.getInstance().getSkillFacade().renderSkillDescription(living, descriptionTemplate, descriptionArguments);
    }

    @Override
    public long getCooldown(LivingEntity living) {
        return RPGClass.getInstance().getSkillFacade().getSkillCooldown(living, cooldownExpression);
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public double getResourceCost(LivingEntity living) {
        return RPGClass.getInstance().getSkillFacade().getSkillResourceCost(living, resourceCostExpression);
    }

    public void setCooldownExpression(String cooldownExpression) {
        this.cooldownExpression = cooldownExpression;
    }

    public void setResourceCostExpression(String resourceCostExpression) {
        this.resourceCostExpression = resourceCostExpression;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    protected void setDescriptionArguments(Map.Entry<String, ?>[] descriptionArguments) {
        this.descriptionArguments = descriptionArguments;
    }

    //===========================================================================================================
    // Holds different types of data E.X.( DAMAGE, RANGE, ETC..)
    //===========================================================================================================
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String propertyKey, Class<T> asClass, T defaultValue) {
        if (String.class.equals(asClass)) {
            return (T) properties.getOrDefault(propertyKey, (String) defaultValue);
        }

        if (Integer.class.equals(asClass)) {
            return (T) ConversionUtils.valueOf(properties.get(propertyKey), (Integer) defaultValue);
        }

        if (Double.class.equals(asClass)) {
            return (T) ConversionUtils.valueOf(properties.get(propertyKey), (Double) defaultValue);
        }

        if (Float.class.equals(asClass)) {
            return (T) ConversionUtils.valueOf(properties.get(propertyKey), (Float) defaultValue);
        }

        if (Long.class.equals(asClass)) {
            return (T) ConversionUtils.valueOf(properties.get(propertyKey), (Long) defaultValue);
        }

        if (Byte.class.equals(asClass)) {
            return (T) ConversionUtils.valueOf(properties.get(propertyKey), (Byte) defaultValue);
        }

        if (Short.class.equals(asClass)) {
            return (T) ConversionUtils.valueOf(properties.get(propertyKey), (Short) defaultValue);
        }

        return defaultValue;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Castable)) return false;
        Castable skill = (Castable) o;
        return id.equals(skill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
