package com.outcast.rpgclass.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgcore.config.ConfigService;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class RPGClassConfig extends ConfigService {

    @JsonProperty("item-damage-types")
    public Map<Material, String> ITEM_DAMAGE_TYPES = new HashMap<>();

    @JsonProperty("offhand-items")
    public Set<Material> OFFHAND_ITEMS = new HashSet<>();

    @JsonProperty("mainhand-item-types")
    public Set<Material> MAINHAND_ITEMS = new HashSet<>();

    @JsonProperty("projectile-damage-types")
    public Map<EntityType, String> PROJECTILE_DAMAGE_TYPES = new HashMap<>();

    @JsonProperty("physical-damage-mitigation-calculation")
    public String PHYSICAL_DAMAGE_MITIGATION_CALCULATION = "1.33 * SOURCE_CON";

    @JsonProperty("magical-damage-mitigation-calculation")
    public String MAGICAL_DAMAGE_MITIGATION_CALCULATION = "1.33 * SOURCE_INT";

    @JsonProperty("damage-production-calculations")
    public Map<String, String> DAMAGE_CALCULATIONS = new HashMap<>();

    @JsonProperty("environmental-damage-calculations")
    public Map<DamageCause, String> ENVIRONMENTAL_CALCULATIONS = new HashMap<>();

    @JsonProperty("default-melee-damage-type")
    public String DEFAULT_MELEE_TYPE = "unarmed";

    @JsonProperty("default-ranged-damage-type")
    public String DEFAULT_RANGED_TYPE = "ranged";

    @JsonProperty("health-regen-calculation")
    public String HEALTH_REGEN_CALCULATION = "1.33 * SOURCE_CON";

    @JsonProperty("health-regen-duration-in-ticks")
    public long HEALTH_REGEN_DURATION_TICKS = 1;

    @JsonProperty("resource-regen-calculation")
    public String RESOURCE_REGEN_CALCULATION = "1.33 * SOURCE_INT";

    @JsonProperty("resource-limit-calculation")
    public String RESOURCE_LIMIT_CALCULATION = "100.0 + SOURCE_INT * 1.5";

    @JsonProperty("health-limit-calculation")
    public String HEALTH_LIMIT_CALCULATION = "100.0 + SOURCE_CON * 1.5";

    @JsonProperty("health-scaling")
    public double HEALTH_SCALING = 20.0;

    @JsonProperty("movement-speed-calculation")
    public String MOVEMENT_SPEED_CALCULATION = "0.1";

    @JsonProperty("attribute-upgrade-cost")
    public String ATTRIBUTE_UPGRADE_COST = "100.0";

    @JsonProperty("experience-max")
    public double EXPERIENCE_MAX = 100_000.0;

    @JsonProperty("experience-min")
    public double EXPERIENCE_MIN = 0.0;

    @JsonProperty("experience-start")
    public double EXPERIENCE_START = 0.0;

    @JsonProperty("attribute-max")
    public double ATTRIBUTE_MAX = 99.0;

    @JsonProperty("attribute-min")
    public double ATTRIBUTE_MIN = 0.0;

    @JsonProperty("experience-spending-limit")
    public double EXPERIENCE_SPENDING_LIMIT = 100_000.0;

    @JsonProperty("attribute-spending-limit")
    public double ATTRIBUTE_SPENDING_LIMIT = 100_000.0;

    @JsonProperty("skill-spending-limit")
    public double SKILL_SPENDING_LIMIT = 100_000.0;

    @JsonProperty("display-root-skill")
    public boolean DISPLAY_ROOT_SKILL = true;

    @JsonProperty("skill-message-distance")
    public double SKILL_MESSAGE_DISTANCE = 25;

    @JsonProperty("max-reward-distance")
    public double MAX_REWARD_DISTANCE = 30.0;

    @JsonProperty("players-keep-inventory-on-pvp")
    public boolean PLAYERS_KEEP_INVENTORY_ON_PVP = false;

    {
        // Wood
        ITEM_DAMAGE_TYPES.put(Material.WOODEN_HOE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.WOODEN_SHOVEL, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.WOODEN_PICKAXE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.WOODEN_AXE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.WOODEN_SWORD, "blunt");

        // Stone
        ITEM_DAMAGE_TYPES.put(Material.STONE_HOE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.STONE_SHOVEL, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.STONE_PICKAXE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.STONE_AXE, "slash");
        ITEM_DAMAGE_TYPES.put(Material.STONE_SWORD, "slash");

        // Iron
        ITEM_DAMAGE_TYPES.put(Material.IRON_HOE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.IRON_SHOVEL, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.IRON_PICKAXE, "stab");
        ITEM_DAMAGE_TYPES.put(Material.IRON_AXE, "slash");
        ITEM_DAMAGE_TYPES.put(Material.IRON_SWORD, "stab");

        // Gold
        ITEM_DAMAGE_TYPES.put(Material.GOLDEN_HOE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.GOLDEN_SHOVEL, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.GOLDEN_PICKAXE, "stab");
        ITEM_DAMAGE_TYPES.put(Material.GOLDEN_AXE, "slash");
        ITEM_DAMAGE_TYPES.put(Material.GOLDEN_SWORD, "stab");

        // Diamond
        ITEM_DAMAGE_TYPES.put(Material.DIAMOND_HOE, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.DIAMOND_SHOVEL, "blunt");
        ITEM_DAMAGE_TYPES.put(Material.DIAMOND_PICKAXE, "stab");
        ITEM_DAMAGE_TYPES.put(Material.DIAMOND_AXE, "slash");
        ITEM_DAMAGE_TYPES.put(Material.DIAMOND_SWORD, "stab");

        // Hand
        ITEM_DAMAGE_TYPES.put(Material.AIR, "unarmed");
    }

    {
        // Bow
        PROJECTILE_DAMAGE_TYPES.put(EntityType.ARROW, "ranged");
    }

    {
        DAMAGE_CALCULATIONS.put("blunt", "CLAMP(SOURCE_CON, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("stab", "CLAMP(SOURCE_STR, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("slash", "CLAMP(SOURCE_STR, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("unarmed", "CLAMP(SOURCE_INT, 1.0, 15.0)");
        DAMAGE_CALCULATIONS.put("ranged", "CLAMP(SOURCE_DEX, 1.0, 15.0)");
    }

    public RPGClassConfig() throws IOException {
        super("config/rpgclass", "config.json", FileType.JSON);
    }

}
