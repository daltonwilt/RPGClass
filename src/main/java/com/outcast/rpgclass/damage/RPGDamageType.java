package com.outcast.rpgclass.damage;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class RPGDamageType {

    private String id = "";
    private String name = "";

    private DamageCause type = DamageCause.CUSTOM;

    public RPGDamageType(String id, String name, DamageCause type) {
        this.id = id;
        this.name = name;
        this.type = type;
        RPGDamageTypeRegistry.getInstance().flags.put(id, this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DamageCause getType() {
        return type;
    }

}
