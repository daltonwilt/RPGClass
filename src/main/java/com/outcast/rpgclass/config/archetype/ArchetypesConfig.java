package com.outcast.rpgclass.config.archetype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgcore.config.ConfigService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ArchetypesConfig extends ConfigService{

    @JsonProperty("classes")
    public Set<ClassConfig> CLASS_CONFIGS = new HashSet<>();

    {
        CLASS_CONFIGS.add(new ClassConfig());
    }

    @JsonProperty("archetypes")
    public Set<ArchetypeConfig> ARCHETYPES = new HashSet<>();

    {
        ARCHETYPES.add(new ArchetypeConfig());
    }

    @JsonProperty("default-archetype")
    public String DEFAULT = "Adventurer";

    public ArchetypesConfig() throws IOException {
        super("config/rpgclass/", "archetypes.json", ConfigService.FileType.JSON);
    }

}
