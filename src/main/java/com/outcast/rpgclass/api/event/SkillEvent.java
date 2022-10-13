package com.outcast.rpgclass.api.event;

import com.outcast.rpgclass.api.skill.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//===========================================================================================================
// Event that will trigger for when a character gains or loses a skill
//===========================================================================================================
public class SkillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Skill skill;

    public SkillEvent(Player player, Skill skill) {
        this.player = player;
        this.skill = skill;
    }

    public Player getTargetPlayer() {
        return player;
    }

    public Skill getSkill() {
        return skill;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
    public static  HandlerList getHandlerList() {
        return handlers;
    }

}
