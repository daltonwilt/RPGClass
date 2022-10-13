package com.outcast.rpgclass.config.archetype;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class ClassConfig {

    @JsonProperty("name")
    public String NAME = "Adventurer";

    @JsonProperty("skills")
    public Set<String> SKILLS = new HashSet<>();

    @JsonProperty("description")
    public String DESCRIPTION = "Basic class when logging in has no skills.";

}
