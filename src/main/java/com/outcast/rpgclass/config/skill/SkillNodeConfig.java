package com.outcast.rpgclass.config.skill;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class SkillNodeConfig {

    @JsonProperty("skill-id")
    public String SKILL_ID = "root-skill";

    @JsonProperty("skill-name")
    public String SKILL_NAME = "";

    @JsonProperty("level")
    public String LEVEL = "1";

    @JsonProperty("cost")
    public String COST_EXPRESSION = "0";

    @JsonProperty("cooldown")
    public String COOLDOWN_EXPRESSION = "0";

    @JsonProperty("properties")
    public Map<String, String> PROPERTIES = new HashMap<>();

}
