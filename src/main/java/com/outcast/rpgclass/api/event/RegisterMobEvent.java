package com.outcast.rpgclass.api.event;

import com.outcast.rpgclass.service.MobService;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//===========================================================================================================
// Event for registering mobs data for in game experience
//===========================================================================================================
public class RegisterMobEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private MobService mobService;

    public RegisterMobEvent(MobService mobService) {
        this.mobService = mobService;
    }

    public void registerMob(String id, LivingEntity mob) {
         mobService.registerMob(id, mob);
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static  HandlerList getHandlerList() {
        return handlers;
    }

}
