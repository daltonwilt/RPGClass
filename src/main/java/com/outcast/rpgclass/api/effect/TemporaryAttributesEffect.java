package com.outcast.rpgclass.api.effect;

import com.outcast.rpgclass.RPGClass;
import com.outcast.rpgclass.api.character.RPGCharacter;
import com.outcast.rpgclass.api.event.ChangeAttributeEvent;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgskill.api.effect.ApplyableCarrier;
import com.outcast.rpgskill.api.effect.TemporaryEffect;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.stream.Collectors;

public class TemporaryAttributesEffect extends TemporaryEffect {

    private Map<AttributeType, Double> attributeIncreases;
    private Map<AttributeType, Double> attributeDecreases;

    public TemporaryAttributesEffect(String id, String name, long duration, Map<AttributeType, Double> attributes, boolean isPositive) {
        super(id, name, duration, isPositive);
        attributeIncreases = attributes;
        attributeDecreases = attributes.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() * -1));
    }

    @Override
    protected boolean apply(ApplyableCarrier<?> character) {
        character.getLivingEntity().ifPresent(living -> {
            RPGCharacter<?> rpgCharacter = RPGClass.getInstance().getCharacterService().getOrCreateCharacter(living);
            RPGClass.getInstance().getAttributeFacade().mergeBuffAttributes(rpgCharacter, attributeIncreases);
            Bukkit.getPluginManager().callEvent(new ChangeAttributeEvent(rpgCharacter));
        });

        return true;
    }

    @Override
    protected boolean remove(ApplyableCarrier<?> character) {
        character.getLivingEntity().ifPresent(living -> {
            RPGCharacter<?> rpgCharacter = RPGClass.getInstance().getCharacterService().getOrCreateCharacter(living);
            RPGClass.getInstance().getAttributeFacade().mergeBuffAttributes(rpgCharacter, attributeDecreases);
            Bukkit.getPluginManager().callEvent(new ChangeAttributeEvent(rpgCharacter));
        });

        return true;
    }

}
