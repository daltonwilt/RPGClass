package com.outcast.rpgclass.damage;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class RPGDamageTypes {

    // Melee
    public static final RPGDamageType CRUSH = new RPGDamageType("rpg:blunt", "Crush", DamageCause.ENTITY_ATTACK);
    public static final RPGDamageType STAB = new RPGDamageType("rpg:stab", "Stab", DamageCause.ENTITY_ATTACK);
    public static final RPGDamageType SLASH = new RPGDamageType("rpg:slash", "Slash", DamageCause.ENTITY_ATTACK);
    public static final RPGDamageType UNARMED = new RPGDamageType("rpg:unarmed", "Unarmed", DamageCause.ENTITY_ATTACK);

    // Ranged
    public static final RPGDamageType ARROW = new RPGDamageType("rpg:arrow", "Arrow", DamageCause.PROJECTILE);
    public static final RPGDamageType BOLT = new RPGDamageType("rpg:bolt", "Bolt", DamageCause.PROJECTILE);
    public static final RPGDamageType THROWN = new RPGDamageType("rpg:thrown", "Thrown", DamageCause.PROJECTILE);

    // Magic
    public static final RPGDamageType SHOCK = new RPGDamageType("rpg:shock", "Shock", DamageCause.MAGIC);
    public static final RPGDamageType ICE = new RPGDamageType("rpg:ice", "Ice", DamageCause.MAGIC);
    public static final RPGDamageType NATURE = new RPGDamageType("rpg:nature", "Nature", DamageCause.MAGIC);
    public static final RPGDamageType FIRE = new RPGDamageType("rpg:fire", "Fire", DamageCause.MAGIC);
    public static final RPGDamageType ILLUSION = new RPGDamageType("rpg:illusion", "Illusion", DamageCause.MAGIC);
    public static final RPGDamageType RADIANT = new RPGDamageType("rpg:radiant", "Radiant", DamageCause.MAGIC);
    public static final RPGDamageType NECROTIC = new RPGDamageType("rpg:necrotic", "Necrotic", DamageCause.MAGIC);

    private RPGDamageTypes() {}

}
