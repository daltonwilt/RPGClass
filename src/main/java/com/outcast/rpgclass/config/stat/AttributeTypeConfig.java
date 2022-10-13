package com.outcast.rpgclass.config.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.ChatColor;

public class AttributeTypeConfig {

    @JsonProperty("id")
    private String id = "NoId";

    @JsonProperty("name")
    private String name = "NoNameAttribute";

    @JsonProperty("short-name")
    private String shortName = "NoShortName";

    @JsonProperty("description")
    private String description = "";

    @JsonProperty("upgradable")
    private boolean upgradable = false;

    @JsonProperty("hidden")
    private boolean hidden = false;

    @JsonProperty("color")
    private ChatColor color = ChatColor.BLACK;

    @JsonProperty("default-value")
    private double defaultValue = 0.0d;

    @JsonProperty("reset-on-login")
    private boolean resetOnLogin = false;

    @JsonProperty("display")
    private String display = "";

    public AttributeTypeConfig() {
    }

    public AttributeTypeConfig(String id, String shortName, String name, String description, boolean upgradable,
                               boolean hidden, ChatColor color, double defaultValue, boolean resetOnLogin, String display) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.description = description;
        this.upgradable = upgradable;
        this.hidden = hidden;
        this.color = color;
        this.defaultValue = defaultValue;
        this.resetOnLogin = resetOnLogin;
        this.display = display;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isUpgradable() {
        return upgradable;
    }

    public boolean isHidden() {
        return hidden;
    }

    public ChatColor getColor() {
        return color;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public boolean isResetOnLogin() {
        return resetOnLogin;
    }

    public String getDisplay() {
        return display;
    }

}
