package com.outcast.rpgclass.command.exception;

import com.outcast.rpgclass.RPGClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;

public class RPGCommandException extends CommandException {

    public RPGCommandException(Object... message) {
        super(RPGClass.getInstance().getRPGMessagingFacade().formatError(ChatColor.RED + message.toString()).toString());
    }
}
