package com.outcast.rpgclass.character;

import com.outcast.rpgclass.api.character.RPGCharacter;
import com.outcast.rpgclass.api.skill.Skill;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgclass.repositroy.AttributeTypeConverter;
import com.outcast.rpgclass.repositroy.RoleConverter;
import com.outcast.rpgclass.repositroy.SkillConverter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.*;

//===========================================================================================================
// Class that stores data for a player with Getters/Setters to manipulate or use data in other resources
//
// Store data in sql using jpa/hibernate
//===========================================================================================================

@Entity
@Table(schema = "rpgclass", name = "Character")
public class Character implements RPGCharacter<Player> {

    @Id
    private UUID id;

    @Transient
    private Player player;

    @Transient
    private boolean hasJoined;

    /**
     * Attributes the player has leveled up, not including the default attribute amount
     * from the configuration
     * Only Upgradable Attributes should be stored within this map
     */

    @Convert(attributeName = "key.", converter = AttributeTypeConverter.class)
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "attribute_type")
    @Column(name = "value")
    @CollectionTable(schema = "rpgclass", name = "Character_attributes")
    private Map<AttributeType, Double> characterAttributes = new HashMap<>();

    /**
     * Attributes that come from temporary sources
     */
    @Transient
    private Map<AttributeType, Double> externalAttributes = new HashMap<>();

    private int level;
    private double experience;
    private double upgradeExperience;
    private double spentUpgradeExperience;
    private double spentAttributeUpgradeExperience;
    private double spentSkillUpgradeExperience;

    /**
     * List of skills acquired by player
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "rpgclass", name = "Character_skills")
    @Convert(converter = SkillConverter.class)
    private List<Skill> skills = new ArrayList<>();

    @Convert(converter = RoleConverter.class)
    private Role role = Role.adventurer;

    public Character() {}

    public Character(UUID uniqueId) {
        this.id = uniqueId;
    }

    @Nonnull
    public UUID getId() {
        return id;
    }

    @Override
    public Optional<Player> getLivingEntity() {
        return Optional.ofNullable(player);
    }

    public void setLivingEntity(Player player) {
        this.player = player;
    }

    public boolean isHasJoined() {
        return hasJoined;
    }

    public void setHasJoined(boolean hasJoined) {
        this.hasJoined = hasJoined;
    }

    @Override
    public void setCharacterAttribute(AttributeType type, Double value) {
        characterAttributes.put(type, value);
    }

    public void addCharacterAttribute(AttributeType type, Double value) {
        characterAttributes.merge(type, value, Double::sum);
    }

    @Override
    public Map<AttributeType, Double> getCharacterAttributes() {
        return Collections.unmodifiableMap(characterAttributes);
    }

    public void setCharacterAttributes(Map<AttributeType, Double> characterAttributes) {
        this.characterAttributes = characterAttributes;
    }

    @Override
    public Map<AttributeType, Double> getExternalAttributes() {
        return Collections.unmodifiableMap(externalAttributes);
    }

    public void mergeExternalAttributes(Map<AttributeType, Double> externalAttributes) {
        externalAttributes.forEach((type , value) -> externalAttributes.merge(type, value, Double::sum));
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public double getUpgradeExperience() {
        return experience;
    }

    public void setUpgradeExperience(double experience) {
        this.experience = experience;
    }

    public double getSpentUpgradeExperience() {
        return spentUpgradeExperience;
    }

    public void setSpentUpgradeExperience(double spentUpgradeExperience) {
        this.spentUpgradeExperience = spentUpgradeExperience;
    }

    public double getSpentAttributeUpgradeExperience() {
        return spentAttributeUpgradeExperience;
    }

    public void setSpentAttributeUpgradeExperience(double spentAttributeUpgradeExperience) {
        this.spentAttributeUpgradeExperience = spentAttributeUpgradeExperience;
    }

    public double getSpentSkillUpgradeExperience() {
        return spentSkillUpgradeExperience;
    }

    public void setSpentSkillUpgradeExperience(double spentSkillUpgradeExperience) {
        this.spentSkillUpgradeExperience = spentSkillUpgradeExperience;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
