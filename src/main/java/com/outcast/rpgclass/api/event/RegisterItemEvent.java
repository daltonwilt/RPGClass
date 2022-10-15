package com.outcast.rpgclass.api.event;

import com.outcast.rpgclass.facade.ItemFacade;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

//===========================================================================================================
// Finish implementation whenever attributes have been developed.
//===========================================================================================================

public class RegisterItemEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private ItemFacade itemFacade;

    public RegisterItemEvent(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

//    public void registerItem(String id, ItemStack item) {
//        itemFacade.registerItem(id, item);
//    }

    public HandlerList getHandlers() {
    return handlers;
    }
    public static  HandlerList getHandlerList() {
        return handlers;
    }

}
