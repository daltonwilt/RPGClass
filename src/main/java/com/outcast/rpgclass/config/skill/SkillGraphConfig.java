package com.outcast.rpgclass.config.skill;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgcore.config.ConfigService;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;

@Singleton
public class SkillGraphConfig extends ConfigService {

    @JsonProperty("skill-nodes")
    public Map<String, SkillNodeConfig> NODES = new HashMap<>();

    @JsonProperty("skill-links")
    public List<SkillNodeLinkConfig> LINKS = new ArrayList<>();

    @JsonProperty("root-skill")
    public SkillNodeConfig ROOT = new SkillNodeConfig();

    @JsonProperty("unique-skills")
    public Set<String> UNIQUE_SKILLS = new HashSet<>();

    public SkillGraphConfig() throws IOException {
        super("config/rpgclass", "skill-graph.json", FileType.JSON);
    }

}
