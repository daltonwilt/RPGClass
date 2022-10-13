package com.outcast.rpgclass.config.loot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgcore.config.ConfigService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTemplatesConfig extends ConfigService {

    @JsonProperty("item-templates")
    public Map<String, List<String>> ITEM_TEMPLATES = new HashMap<>();

    @JsonProperty("rarities")
    public Map<String, String> RARITIES = new HashMap<>();

    @JsonProperty("categories")
    public Map<String, String> CATEGORIES = new HashMap<>();

    public ItemTemplatesConfig() throws IOException {
        super("config/rpgclass", "item-templates.json", FileType.JSON);
    }

}
