package com.outcast.rpgclass.config.mob;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgcore.config.ConfigService;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MobsConfig extends ConfigService {

    @JsonProperty("mobs")
    public Map<String, MobConfig> MOBS = new HashMap<>();

    {
        MOBS.put("zombie", new MobConfig());
    }

    public MobsConfig() throws IOException {
        super("config/rpgclass", "monsters.json", FileType.JSON);
    }

}
