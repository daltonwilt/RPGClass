package com.outcast.rpgclass.config.stat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outcast.rpgcore.config.ConfigService;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttributesConfig extends ConfigService {

    @JsonProperty("attribute-types")
    public List<AttributeTypeConfig> ATTRIBUTE_TYPES = new ArrayList<>();

    {
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:strength",
                "str",
                "Strength",
                "",
                true,
                false,
                ChatColor.RED,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:constitution",
                "con",
                "Constitution",
                "",
                true,
                false,
                ChatColor.YELLOW,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:dexterity",
                "dex",
                "Dexterity",
                "",
                true,
                false,
                ChatColor.GREEN,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:intelligence",
                "int",
                "Intelligence",
                "",
                true,
                false,
                ChatColor.BLUE,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:wisdom",
                "wis",
                "Wisdom",
                "",
                true,
                false,
                ChatColor.LIGHT_PURPLE,
                1.0d,
                false,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:magical_resistance",
                "magicres",
                "Magical Resistance",
                "",
                false,
                false,
                ChatColor.AQUA,
                0.0d,
                true,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:physical_resistance",
                "physres",
                "Physical Resistance",
                "",
                false,
                false,
                ChatColor.GOLD,
                0.0d,
                true,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:base_armor",
                "armor",
                "Base Armor",
                "",
                false,
                true,
                ChatColor.DARK_AQUA,
                0.0d,
                true,
                ""
        ));
        ATTRIBUTE_TYPES.add(new AttributeTypeConfig(
                "rpg:base_damage",
                "dmg",
                "Base Damage",
                "",
                false,
                true,
                ChatColor.DARK_RED,
                0.0d,
                true,
                ""
        ));
    }

    public AttributesConfig() throws IOException {
        super("config/rpgclass", "attributes.json", FileType.JSON);
    }

}
