package com.outcast.rpgclass.config.loot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ItemLootConfig {

    @JsonProperty("item-group")
    public String ITEM_GROUP = null;

    @JsonProperty("item-ids")
    public List<String> ITEM_IDS = new ArrayList<>();

    @JsonProperty("quantity-min")
    public int MINIMUM_QUANTITY = 1;

    @JsonProperty("quantity-max")
    public int MAXIMUM_QUANTITY = 1;

    @JsonProperty("quantity")
    public int QUANTITY = 1;

}
