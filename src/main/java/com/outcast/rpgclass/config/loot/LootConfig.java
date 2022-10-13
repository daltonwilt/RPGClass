package com.outcast.rpgclass.config.loot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LootConfig {

    @JsonProperty("drop-rate")
    public Double DROP_RATE = 0.0d;

//    @JsonProperty("currency")
//    public CurrencyLootConfig CURRENCY;

    @JsonProperty("item")
    public ItemLootConfig ITEM;

    @JsonProperty("experience")
    public ExperienceLootConfig EXPERIENCE;

}
