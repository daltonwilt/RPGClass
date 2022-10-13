package com.outcast.rpgclass.damage;

import com.outcast.rpgclass.RPGClass;
import org.bukkit.Material;

public class RPGDamageService {

    private static RPGDamageService instance = new RPGDamageService();

    private RPGDamageService() {}

    public RPGDamageType getItemDamageType(Material type) {
        return RPGClass.getCombatConfig().DAMAGE_MAP.getOrDefault(type, RPGDamageTypes.UNARMED);
    }

    public static RPGDamageService getInstance() {
        return instance;
    }

}
