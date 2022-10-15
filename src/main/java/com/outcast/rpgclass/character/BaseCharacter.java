package com.outcast.rpgclass.character;

import com.outcast.rpgclass.api.character.RPGCharacter;
import com.outcast.rpgclass.api.stat.AttributeType;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BaseCharacter<T extends LivingEntity> implements RPGCharacter<T> {

    private UUID id;
    private T entity;
    private Map<AttributeType, Double> characterAttributes;
    private Map<AttributeType, Double> externalAttributes;

    public BaseCharacter(T entity, Map<AttributeType, Double> characterAttributes) {
        this.id = entity.getUniqueId();
        this.entity = entity;
        this.characterAttributes = characterAttributes;
        this.externalAttributes = new HashMap<>();
    }

    @Nonnull
    public UUID getId() {
        return id;
    }

    @Override
    public Optional<T> getLivingEntity() {
        return Optional.ofNullable(entity);
    }

    public void setLivingEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public Map<AttributeType, Double> getCharacterAttributes() {
        return characterAttributes;
    }

    @Override
    public void setCharacterAttribute(AttributeType type, Double value) {
        characterAttributes.put(type, value);
    }

    @Override
    public void addCharacterAttribute(AttributeType type, Double amount) {
        characterAttributes.merge(type, amount, Double::sum);
    }

    public void setCharacterAttributes(Map<AttributeType, Double> characterAttributes) {
        this.characterAttributes = characterAttributes;
    }

    @Override
    public Map<AttributeType, Double> getExternalAttributes() {
        return externalAttributes;
    }

    @Override
    public void mergeExternalAttributes(Map<AttributeType, Double> additional) {
        additional.forEach((type, value) -> externalAttributes.merge(type, value, Double::sum));
    }

}
