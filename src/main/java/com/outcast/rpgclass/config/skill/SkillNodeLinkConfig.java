package com.outcast.rpgclass.config.skill;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SkillNodeLinkConfig {

    public enum SkillNodeLinkConfigType {
        UNIDIRECTIONAL,
        BIDIRECTIONAL
    }

    @JsonProperty("parent-node")
    public String PARENT_SKILL_NODE_ID = "some-skill-node-parent-id";

    @JsonProperty("child-node")
    public String CHILD_SKILL_NODE_ID = "some-skill-node-child-id";

    @JsonProperty("cost")
    public String COST = "0.0";

    @JsonProperty("type")
    public SkillNodeLinkConfigType TYPE = SkillNodeLinkConfigType.UNIDIRECTIONAL;

}
