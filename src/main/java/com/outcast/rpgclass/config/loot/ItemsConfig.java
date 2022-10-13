package com.outcast.rpgclass.config.loot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgcore.config.ConfigService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ItemsConfig extends ConfigService {

    @JsonProperty("items")
    public Set<ItemConfig> ITEMS = new HashSet<>();

    {
        ITEMS.add(new ItemConfig());
    }

    public ItemsConfig(String path) throws IOException {
        super("config/rpgclass/items/", path, FileType.JSON);
    }

}
