package com.outcast.rpgclass.api.event;

import com.outcast.rpgclass.api.character.RPGCharacter;
import com.outcast.rpgclass.character.Character;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//===========================================================================================================
// Event for triggering when an attribute has been updated
//===========================================================================================================

public class ChangeAttributeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private RPGCharacter<?> character;

    public ChangeAttributeEvent(RPGCharacter<?> character) {
        this.character = character;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static  HandlerList getHandlerList() {
        return handlers;
    }

}
