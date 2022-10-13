package com.outcast.rpgclass.facade;


import com.google.inject.Singleton;
import com.outcast.rpgcore.util.AbstractMessaging;

@Singleton
public class RPGMessagingFacade extends AbstractMessaging {
    public RPGMessagingFacade() {
        super("Class");
    }
}
