package com.outcast.rpgclass.service;

import com.google.inject.Inject;
import com.outcast.rpgclass.RPGClass;
import com.outcast.rpgclass.config.RPGClassConfig;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class HealingService {

    @Inject
    private RPGClassConfig config;

    @Inject
    private ExpressionService expressionService;

    @Inject
    private CharacterService characterService;

    public void init() {
        // task
        new BukkitRunnable() {
            @Override
            public void run() { tickHealthRegen(); }
        }.runTaskTimer(RPGClass.getInstance(), 0, config.HEALTH_REGEN_DURATION_TICKS);
    }

    public void heal(LivingEntity living, double amount) {
        living.setHealth(Math.min(living.getHealth() + amount, living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }

    private void tickHealthRegen() {
        Bukkit.getServer().getOnlinePlayers().stream().forEach(player -> {
            if(player.getHealth() <= 0)
                return;

            double healthRegenAmount = expressionService.evalExpression(player, config.HEALTH_REGEN_CALCULATION).doubleValue();
            heal(player, healthRegenAmount);
        });
    }

}
