package com.outcast.rpgclass.command;

import com.outcast.rpgcore.command.CommandBuilder;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommandClass extends CommandBuilder{

    public CommandClass() {
        super();
        this.addAlias("class");
        this.setDescription("Testing command service in RPGClass.");
        this.setName("class");
        this.setPermission("rpgclass.class");
        this.setSyntax("/class <args>");
        this.setArguments(new ArrayList<>(Arrays.asList("test1", "test2")));
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        try {
            sender.sendMessage("Testing command service in RPGClass!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}