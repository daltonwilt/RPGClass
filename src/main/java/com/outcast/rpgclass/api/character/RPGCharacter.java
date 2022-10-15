package com.outcast.rpgclass.api.character;

import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgcore.db.Identifiable;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.Optional;

//===========================================================================================================
// Represents an entity that can have attributes
// @param <T> type of Entity
//===========================================================================================================

public interface RPGCharacter<T extends LivingEntity> extends Identifiable {

    Optional<T> getLivingEntity();

    void setLivingEntity(T living);

    /**
     * Gets the current Character Attributes
     * @return Map of Character Attributes
     */
    Map<AttributeType, Double> getCharacterAttributes();

    /**
     * Set a Character attribute to a specific value
     * @param type Attribute to set
     * @param value Value to set to
     */
    void setCharacterAttribute(AttributeType type, Double value);

    /**
     * Add a value to an existing character attribute
     * @param type Attribute to add
     * @param amount Amount to add
     */
    void addCharacterAttribute(AttributeType type, Double amount);

    /**
     * Gets the current External Attributes
     * @return Map of Attributes provided by buffs
     */
    Map<AttributeType, Double> getExternalAttributes();

    /**
     * Merge the values of the two attribute type maps by adding them together
     * @param additional Attributes to add
     */
    void mergeExternalAttributes(Map<AttributeType, Double> additional);

}
