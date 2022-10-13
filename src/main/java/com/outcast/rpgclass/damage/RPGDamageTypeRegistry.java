package com.outcast.rpgclass.damage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RPGDamageTypeRegistry {

    private static final RPGDamageTypeRegistry instance = new RPGDamageTypeRegistry();

    protected Map<String, RPGDamageType> flags = new HashMap<>();

    public static RPGDamageTypeRegistry getInstance() { return instance; }

    public Optional<RPGDamageType> getById(String id) {
        return Optional.ofNullable(flags.get(id));
    }

    public Collection<RPGDamageType> getAll() {
        return flags.values();
    }

}
