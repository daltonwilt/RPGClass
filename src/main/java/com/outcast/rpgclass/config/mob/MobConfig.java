package com.outcast.rpgclass.config.mob;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgclass.config.loot.LootConfig;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MobConfig {

    @JsonProperty("damage-expression")
    public String DAMAGE_EXPRESSION = "5.0";

    @JsonProperty("item-drop-limit")
    public int ITEM_DROP_LIMIT = 2;

    @JsonProperty("entity-type")
    public EntityType ENTITY_TYPE = EntityType.ZOMBIE;

    @JsonProperty("default-attributes")
    public Map<AttributeType, Double> DEFAULT_ATTRIBUTES = new HashMap<>();

    @JsonProperty("loot")
    public Set<LootConfig> LOOT = new HashSet<>();

}
