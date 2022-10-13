package com.outcast.rpgclass.config.loot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgclass.api.stat.AttributeType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConfig {

    @JsonProperty("id")
    public String ID = "unique-item-id";

    @JsonProperty("name")
    public String ITEM_NAME = "&oDefault Item Name";

    @JsonProperty("hide-flags")
    public boolean HIDE_FLAGS = false;

    @JsonProperty("type")
    public Material ITEM_TYPE = Material.DIRT;

    @JsonProperty("durability")
    public int DURABILITY = -1; // if durability is -1, that means full default item durability

    @JsonProperty("lore")
    public List<String> LORE = new ArrayList<>();

    @JsonProperty("template")
    public String TEMPLATE = "";

    @JsonProperty("category")
    public String CATEGORY = "";

    @JsonProperty("rarity")
    public String RARITY = "";

    @JsonProperty("description")
    public String DESCRIPTION = "";

    @JsonProperty("enchantments")
    public Map<Enchantment, Integer> ENCHANTMENTS = new HashMap<>();

    @JsonProperty("attributes")
    public Map<AttributeType, Double> ATTRIBUTES = new HashMap<>();

    @JsonProperty("group")
    public String GROUP = "default";

}
