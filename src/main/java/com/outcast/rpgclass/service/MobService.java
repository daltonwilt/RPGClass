package com.outcast.rpgclass.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpgclass.api.event.RegisterMobEvent;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgclass.config.mob.MobConfig;
import com.outcast.rpgclass.config.mob.MobsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

//===========================================================================================================
// Implement this system after  main class system is finished.
//===========================================================================================================
@Singleton
public class MobService {

    @Inject
    private CharacterService characterService;

    @Inject
    private MobsConfig mobsConfig;

    @Inject
    private AttributeService attributeService;

    private Map<String, EntityType> mobsCache = new HashMap<>();

    public void init() {
        loadMobs();
    }

    private void loadMobs() {
        mobsConfig.MOBS.forEach((id, mobConfig) -> {
//            .createEntity(mobConfig.ENTITY_TYPE, Vector3d.ZERO);
            World w = Bukkit.getServer().getWorld("world");
            Entity entity = w.spawnEntity(new Location(w, 0, 0, 0), mobConfig.ENTITY_TYPE);
            if (entity instanceof LivingEntity) {
                registerMob(id, (LivingEntity) entity);
            }
        });

        // call event
        Bukkit.getPluginManager().callEvent(new RegisterMobEvent(this));
    }

    public void registerMob(String id, LivingEntity living) {
        MobConfig mobConfig = mobsConfig.MOBS.get(id);
        Map<AttributeType, Double> attributes = new HashMap<>(mobConfig.DEFAULT_ATTRIBUTES);

        assignEntityDamageExpression(living, attributes, mobConfig.DAMAGE_EXPRESSION);
        characterService.assignEntityHealthLimit(living, true);

//        mobsCache.put(id, living.createArchetype());
    }

//    public Optional<EntityArchetype> getMob(String id) {
//        return Optional.ofNullable(mobsCache.get(id));
//    }

    public void assignEntityDamageExpression(LivingEntity living, Map<AttributeType, Double> mobAttributes, String damageExpression) {
        Map<AttributeType, Double> attributes = attributeService.fillInAttributes(mobAttributes);
//
        characterService.getOrCreateCharacter(living, attributes);
//        entity.offer(new DamageExpressionData(damageExpression));
    }

}
