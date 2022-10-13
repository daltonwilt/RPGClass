package com.outcast.rpgclass.api.skill;

import net.kyori.adventure.text.TextComponent;

import java.util.HashMap;
import java.util.Map;

//===========================================================================================================
// Class that will be called when creating skill information inside a custom skill
//===========================================================================================================
public class SkillSpec {

    private String id;
    private String name;
    private String permission;
    private TextComponent descriptionTemplate;
    private Map.Entry<String, ?>[] descriptionArguments;
    private String cooldownExpression;
    private String resourceCostExpression;
    private Map<String,String> properties = new HashMap<>();

    protected SkillSpec() {}

    public static SkillSpec create() {
        return new SkillSpec();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public TextComponent getDescriptionTemplate() {
        return descriptionTemplate;
    }

    public Map.Entry<String, ?>[] getDescriptionArguments() {
        return descriptionArguments;
    }

    public String getCooldownExpression() {
        return cooldownExpression;
    }

    public String getResourceCostExpression() {
        return resourceCostExpression;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public SkillSpec id(String id) {
        this.id = id;
        permission = "atherysrpg.rpgskills." + id;
        return this;
    }

    public SkillSpec name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Create a new TextTemplate and set it
     * @param template
     * @return
     */
    public SkillSpec descriptionTemplate(TextComponent template) {
        this.descriptionTemplate = template;
        return this;
    }

    @SafeVarargs
    public final SkillSpec descriptionArguments(Map.Entry<String, ?>... descriptionArguments) {
        this.descriptionArguments = descriptionArguments;
        return this;
    }

    public SkillSpec cooldown(String cooldownExpression) {
        this.cooldownExpression = cooldownExpression;
        return this;
    }

    public SkillSpec resourceCost(String resourceCostExpression) {
        this.resourceCostExpression = resourceCostExpression;
        return this;
    }

    public SkillSpec properties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    public SkillSpec property(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

}
