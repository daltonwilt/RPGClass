package com.outcast.rpgclass.api.skill;

import com.outcast.rpgclass.util.description.*;

public final class DescriptionArguments {

    public static DescriptionArgument ofSource(String expression) {
        return new ExpressionDescription(expression);
    }

    public static DescriptionArgument ofProperty(Skill skill, String propertyName, String defaultValue) {
        return new PropertyDescription(skill, propertyName, defaultValue);
    }

    public static DescriptionArgument ofTimeProperty(Skill skill, String propertyName, String defaultValue) {
        return new TimePropertyDescription(skill, propertyName, defaultValue);
    }

    public static SpeedPropertyDescription ofSpeedPercentProperty(Skill skill, String propertyName, String defaultValue) {
        return new SpeedPropertyDescription(skill, propertyName, defaultValue);
    }

    public static SlowPropertyDescription ofSlowPercentProperty(Skill skill, String propertyName, String defaultValue) {
        return new SlowPropertyDescription(skill, propertyName, defaultValue);
    }

}