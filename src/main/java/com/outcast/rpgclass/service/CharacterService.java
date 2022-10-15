package com.outcast.rpgclass.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpgclass.RPGClass;
import com.outcast.rpgclass.api.character.RPGCharacter;
import com.outcast.rpgclass.api.event.ChangeAttributeEvent;
import com.outcast.rpgclass.api.skill.Skill;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgclass.character.BaseCharacter;
import com.outcast.rpgclass.character.Role;
import com.outcast.rpgclass.character.Character;
import com.outcast.rpgclass.config.RPGClassConfig;
import com.outcast.rpgclass.config.archetype.ArchetypesConfig;
import com.outcast.rpgclass.config.archetype.ClassConfig;
import com.outcast.rpgclass.repositroy.CharacterRepository;
import com.outcast.rpgskill.RPGSkill;
import com.outcast.rpgskill.api.resource.ResourceEntity;
import com.outcast.rpgskill.api.skill.Castable;
import com.udojava.evalex.Expression;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class CharacterService {

    @Inject
    private RPGClassConfig config;

    @Inject
    private ArchetypesConfig archetypesConfig;

    @Inject
    private CharacterRepository repository;

    @Inject
    private AttributeService attributeService;

    @Inject
    private ExpressionService expressionService;

    @Inject
    private SkillGraphService skillGraphService;

    private final Map<UUID, RPGCharacter<? extends LivingEntity>> npcs = new HashMap<>();
    private final Map<String, Role> classes = new HashMap<>();

    public void init() {
        for(ClassConfig classConfig : archetypesConfig.CLASS_CONFIGS) {
            Role role = new Role(
                    classConfig.NAME,
                    classConfig.SKILLS.stream()
                            .map(skillId -> (Skill) RPGSkill.getInstance().getSkillService().getSkillById(skillId).get())
                            .collect(Collectors.toSet()),
                    classConfig.DESCRIPTION
            );
            classes.put(classConfig.NAME, role);
        }
    }

    public Character getOrCreateCharacter(Player player) {
//        return repository.findById(player.getUniqueId()).orElseGet(() -> {
//            Character character = new Character(player.getUniqueId());
//            character.setEntity(player);
//            character.addSkill(skillGraphService.getSkillGraphRoot());
//            character.setExperience(config.EXPERIENCE_START);
//            repository.saveOne(c);
//
//            return c;
//        });

        return null;
    }

    public RPGCharacter<? extends LivingEntity> getOrCreateCharacter(LivingEntity living, Map<AttributeType, Double> attributes) {
        if (living instanceof Player) {
            getOrCreateCharacter((Player) living);
        }

        RPGCharacter<? extends LivingEntity> npc = npcs.get(living.getUniqueId());

        if (npc == null) {
            npc = new BaseCharacter(living, attributes);
            npcs.put(living.getUniqueId(), npc);
        }

        return npc;
    }

    public <T extends Entity> RPGCharacter<?> getOrCreateCharacter(T entity) {
        if (entity instanceof LivingEntity) {
            return getOrCreateCharacter((LivingEntity) entity, attributeService.getDefaultAttributes());
        } else {
            throw new IllegalArgumentException("Entity must be some sort of Living.");
        }
    }

    public void setSkills(Character character, List<Skill> skills) {
        setSkillPermissions(character, character.getSkills(), false);
        character.getSkills().clear();
        character.addSkill(skillGraphService.getSkillGraphRoot());

        setSkillPermission(character, skillGraphService.getSkillGraphRoot(), true);
        character.getSkills().addAll(skills);

        setSkillPermissions(character, skills, true);
//        repository.saveOne(c);
    }

    public void addSkill(Character character, Skill skill) {
        character.addSkill(skill);
        setSkillPermission(character, skill, true);
//        repository.saveOne(c);
    }

    public void removeSkill(Character character, Skill skill) {
        character.removeSkill(skill);
        setSkillPermission(character, skill, false);
//        repository.saveOne(character);
    }

    private void setSkillPermission(Character character, Castable skill, boolean value) {
        getPlayer(character).ifPresent(player -> {
            PermissionAttachment pa = player.addAttachment(RPGClass.getInstance());
            pa.setPermission(skill.getPermission(), true);
        });
    }

    private void setSkillPermissions(Character character, Collection<? extends Castable> skills, boolean value) {
        getPlayer(character).ifPresent(player -> {
            PermissionAttachment pa = player.addAttachment(RPGClass.getInstance());
            skills.forEach(skill -> {
                pa.setPermission(skill.getPermission(), true);
            });
        });
    }

    private Optional<Player> getPlayer(Character character) {
        return character.getLivingEntity();
    }

    // Add level & experience method additions

    public void addUpgradeExperience(Character character, double amount) {
        character.setUpgradeExperience(character.getUpgradeExperience() + amount);
//        repository.saveOne(c);
    }

    public void removeUpgradeExperience(Character character, double amount) {
        character.setUpgradeExperience(character.getUpgradeExperience() - amount);
//        repository.saveOne(c);
    }

    /**
     * Increase the value of an attribute directly on a character
     * @param character The Character to remove the attribute from
     * @param attributeType The attribute to increase
     * @param amount The amount to increase
     */
    public void addAttribute(Character character, AttributeType attributeType, double amount) {
        character.addCharacterAttribute(attributeType, amount);
//        repository.saveOne(c);

        // call event
        Bukkit.getPluginManager().callEvent(new ChangeAttributeEvent(character));
    }

    /**
     * Set the value of an attribute directly on a Character
     * @param character The Character to remove the attribute from
     * @param attributeType The attribute to set
     * @param amount The amount to set
     */
    public void setAttribute(Character character, AttributeType attributeType, double amount) {
        character.setCharacterAttribute(attributeType, amount);
//        repository.saveOne(c);

        // call event
        Bukkit.getPluginManager().callEvent(new ChangeAttributeEvent(character));
    }

    /**
     * Remove an Attribute from a Character
     * @param character The Character to remove the attribute from
     * @param attributeType The attribute to decrease
     * @param amount The amount to decrease by
     */
    public void removeAttribute(Character character, AttributeType attributeType, double amount) {
        Double cur = character.getCharacterAttributes().getOrDefault(attributeType, 0.0d);
        character.setCharacterAttribute(attributeType, Math.max(0.0d, cur - amount));
//        repository.saveOne(character);

        // call event
        Bukkit.getPluginManager().callEvent(new ChangeAttributeEvent(character));
    }

    public void mergeBuffAttributes(RPGCharacter<?> character, Map<AttributeType, Double> attributes) {
        character.mergeExternalAttributes(attributes);
    }

    public void addSpentUpgradeExperience(Character character, double amount) {
        character.setSpentUpgradeExperience(character.getSpentUpgradeExperience() + amount);
//        repository.saveOne(character);
    }

    public void addSpentSkillUpgradeExperience(Character character, double amount) {
        character.setSpentUpgradeExperience(character.getSpentUpgradeExperience() + amount);
        character.setSpentSkillUpgradeExperience(character.getSpentSkillUpgradeExperience() + amount);
//        repository.saveOne(character);
    }

    public void addSpentAttributeUpgradeExperience(Character character, double amount) {
        character.setSpentUpgradeExperience(character.getSpentUpgradeExperience() + amount);
        character.setSpentAttributeUpgradeExperience(character.getSpentAttributeUpgradeExperience() + amount);
//        repository.saveOne(character);

        // call event
        Bukkit.getPluginManager().callEvent(new ChangeAttributeEvent(character));
    }

    public double calcResourceRegen(Map<AttributeType, Double> attributes) {
        Expression expression = expressionService.getExpression(config.RESOURCE_REGEN_CALCULATION);
        expressionService.populateSourceAttributes(expression, attributes);
        return expression.eval().doubleValue();
    }

    public void resetCharacterSkills(Character character) {
        double spentOnSkills = character.getSpentSkillUpgradeExperience();

        setSkills(character, new ArrayList<>(character.getRole().getSkills()));

        character.setSpentSkillUpgradeExperience(0);
        character.setSpentUpgradeExperience(character.getSpentUpgradeExperience() - spentOnSkills);
        character.setUpgradeExperience(character.getUpgradeExperience() + spentOnSkills);
//        repository.saveOne(c);
    }

    public void resetCharacterAttributes(Character character) {
        double spentOnAttributes = character.getSpentAttributeUpgradeExperience();

        character.setCharacterAttributes(new HashMap<>());
        character.setSpentAttributeUpgradeExperience(0);
        character.setSpentUpgradeExperience(character.getSpentUpgradeExperience() - spentOnAttributes);
        character.setUpgradeExperience(character.getUpgradeExperience() + spentOnAttributes);
//        repository.saveOne(character);

        // call event
        Bukkit.getPluginManager().callEvent(new ChangeAttributeEvent(character));
    }

    public void updateRole(Character character, Role role) {
        character.setRole(role);
        resetCharacterSkills(character);
    }

    public Optional<Role> getRole(String classId) {
        return Optional.ofNullable(classes.get(classId));
    }

    public Collection<Role> getAllRoles() {
        return classes.values();
    }

    /**
     * Resets the characters attributes and skills, and gives back used experience.
     */
    public void resetCharacter(Character character) {
        resetCharacterSkills(character);
        resetCharacterAttributes(character);
    }

    public void assignEntityResourceLimit(LivingEntity living, boolean fill) {
        double max = expressionService.evalExpression(living, config.RESOURCE_LIMIT_CALCULATION).doubleValue();
        ResourceEntity user = RPGSkill.getInstance().getResourceService().getOrCreateEntity(living);

        user.setMax(max);
        if (fill) {
            user.fill();
        } else if (user.getCurrent() > user.getMax()) {
            user.fill();
        }
    }

    public void assignEntityHealthLimit(LivingEntity living, boolean fill) {
        double maxHP = expressionService.evalExpression(living, config.HEALTH_LIMIT_CALCULATION).doubleValue();

//        DataTransactionResult maxHPResult = living.offer(Keys.MAX_HEALTH, maxHP);
        if (fill) {
//            living.offer(Keys.HEALTH, maxHP);
        }
//
//        if (!maxHPResult.isSuccessful()) {
//            RPGClass.getInstance().warn(
//                    "Failed to set max health for entity {}, Max HP Result: {}",
//                    living,
//                    maxHPResult
//            );
//        }
//
//        if (living.supports(Keys.HEALTH_SCALE)) {
//            living.offer(Keys.HEALTH_SCALE, config.HEALTH_SCALING);
//        }
    }

    public void assignEntityMovementSpeed(LivingEntity living) {
//        if (!living.supports(Keys.WALKING_SPEED)) return;
//
//        double newMovementSpeed = expressionService.evalExpression(living, config.MOVEMENT_SPEED_CALCULATION).doubleValue();
//        double oldMovementSpeed = living.get(Keys.WALKING_SPEED).get();
//
//        if (Math.abs(newMovementSpeed - oldMovementSpeed) >= 0.0001) {
//            living.offer(Keys.WALKING_SPEED, newMovementSpeed);
//        }
    }

}
