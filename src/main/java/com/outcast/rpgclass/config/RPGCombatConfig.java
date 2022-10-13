package com.outcast.rpgclass.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgclass.damage.RPGDamageType;
import com.outcast.rpgclass.damage.RPGDamageTypes;
import com.outcast.rpgcore.config.ConfigService;
import org.bukkit.Material;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class RPGCombatConfig extends ConfigService {

    @JsonProperty("enabled")
    public boolean ENABLED = false;

    @JsonProperty("damage_per_type")
    public Map<Material, RPGDamageType> DAMAGE_MAP = new HashMap<>();
    {
        DAMAGE_MAP.put(Material.WOODEN_SWORD, RPGDamageTypes.SLASH);
    }

    public RPGCombatConfig() throws IOException {
        super("config/rpgclass", "combat.json", FileType.JSON);
    }

}
