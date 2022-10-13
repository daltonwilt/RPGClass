package com.outcast.rpgclass.config.archetype;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class ArchetypeConfig {

    @JsonProperty("display-name")
    public String NAME = "Adventurer";

    @JsonProperty("skills")
    public Set<String> SKILLS = new HashSet<>();

}
