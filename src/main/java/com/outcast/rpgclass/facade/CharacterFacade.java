package com.outcast.rpgclass.facade;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpgclass.api.event.SkillEvent;
import com.outcast.rpgclass.api.skill.Skill;
import com.outcast.rpgclass.character.Character;
import com.outcast.rpgclass.character.Role;
import com.outcast.rpgclass.command.exception.RPGCommandException;
import com.outcast.rpgclass.config.RPGClassConfig;
import com.outcast.rpgclass.config.archetype.ArchetypeConfig;
import com.outcast.rpgclass.config.archetype.ArchetypesConfig;
import com.outcast.rpgclass.service.*;
import com.outcast.rpgskill.api.event.ResourceEvent;
import com.udojava.evalex.Expression;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

//===========================================================================================================
// Facade class that usable methods by entities for skills and their characters
//===========================================================================================================

@Singleton
public class CharacterFacade {

    @Inject
    private RPGClassConfig config;

    @Inject
    private ArchetypesConfig archetypesConfig;

    @Inject
    private DamageService damageService;

    @Inject
    private HealingService healingService;

    @Inject
    private ExpressionService expressionService;

    @Inject
    private CharacterService characterService;

    @Inject
    private AttributeFacade attributeFacade;

    @Inject
    private SkillFacade skillFacade;

    @Inject
    private SkillGraphService skillGraphService;

    @Inject
    private RPGMessagingFacade rpgMsg;

    public void showPlayerUpgradeExperience(Player player) {
        Character character = characterService.getOrCreateCharacter(player);
        rpgMsg.info((Audience)player, Component.text(ChatColor.DARK_GRAY + "Your current experience: " + ChatColor.DARK_GREEN + (int) character.getUpgradeExperience()));
    }

    public void addPlayerUpgradeExperience(Player player, double amount) {
        Character character = characterService.getOrCreateCharacter(player);

        if(validateExperience(character.getUpgradeExperience() + amount)) {
            characterService.removeUpgradeExperience(character, amount);
            rpgMsg.info((Audience)player, Component.text("Gained " + ChatColor.DARK_GREEN + (int) amount + ChatColor.DARK_GRAY + " experience."));
        }
    }

    public void removePlayerUpgradeExperience(Player player, double amount) {
        Character character = characterService.getOrCreateCharacter(player);

        if(validateExperience(character.getUpgradeExperience() - amount)) {
            characterService.removeUpgradeExperience(character, amount);
        }
    }

    public void displayAllSkills(Player player) throws RPGCommandException {
        Character character = characterService.getOrCreateCharacter(player);
        checkClass(character);

        List<Skill> skills = config.DISPLAY_ROOT_SKILL ? character.getSkills() : character.getSkills().subList(1, character.getSkills().size());

        List<TextComponent> messages = new ArrayList<>();
        messages.add(Component.text(ChatColor.BOLD + "" + ChatColor.AQUA + getArchetype(skills)));
        messages.add(Component.text(ChatColor.DARK_GRAY + "[]====[ " + ChatColor.AQUA + "Skills" + ChatColor.DARK_GRAY + " ]====[]"));

        for(Skill skill : skills) {
            messages.add(Component.text(ChatColor.DARK_AQUA + "- " + skillFacade.renderSkill(skill, player) + " "));
        }

        messages.add(Component.text(ChatColor.DARK_GRAY + "[]==[ " + ChatColor.RED + "Upgradeable Skills" + ChatColor.DARK_GRAY + " ]==[]"));
        for(Skill skill : skillGraphService.getLinkedSkills(character.getSkills())) {
            TextComponent skillText = Component.text()
                    .append(Component.text(ChatColor.DARK_RED + "- " + skillFacade.renderAvailableSkill(skill, player, true) + " "))
                    .clickEvent(ClickEvent.runCommand(chooseSkillWithoutThrowing(player, skill.getId())))
                    .build();
            messages.add(skillText);
        }

        messages.forEach(message -> ((Audience)player).sendMessage(message, MessageType.CHAT));
    }

    private void checkClass(Character character) throws RPGCommandException {
        if(archetypesConfig.CLASS_CONFIGS.size() > 0 && character.getRole() == Role.adventurer) {
            throw new RPGCommandException("You must choose a class before unlocking skills!");
        }
    }

    public void showClasses(Player player) throws RPGCommandException {
        Character character = characterService.getOrCreateCharacter(player);
        List<TextComponent> messages = new ArrayList<>();
        messages.add(Component.text(ChatColor.DARK_GRAY + "[]======[" + ChatColor.DARK_AQUA + "Classes" + ChatColor.DARK_GRAY + " ]======[]"));

        for(Role role : characterService.getAllRoles()) {
            TextComponent name = Component.text()
                    .append(Component.text(ChatColor.BOLD + "" + ChatColor.AQUA))
//                    .clickEvent()
//                    .hoverEvent()
                    .build();

            messages.add(name);
            TextComponent.Builder skills = Component.text();
            role.getSkills().stream()
                    .map(skill -> skillFacade.renderAvailableSkill(skill, player, false))
                    .forEach(skills::append);
            messages.add(Component.text(ChatColor.RED + "Starting Skills: " + skills.build()));
        }

        messages.forEach(message -> ((Audience)player).sendMessage(message, MessageType.CHAT));
    }

    public void setClass(CommandSender source, Player player, String roleId) throws RPGCommandException {
        Role role = characterService.getRole(roleId).orElseThrow(() -> new RPGCommandException("No class with that name exists!"));
        characterService.updateRole(characterService.getOrCreateCharacter(player), role);
        rpgMsg.info((Audience)source, "Set " + ChatColor.RED + player.getName() + "'s" + ChatColor.DARK_GRAY + " class to " + ChatColor.DARK_RED + role.getName() + ChatColor.DARK_AQUA + ".");
    }

    private String getArchetype(List<Skill> skills) {
        int skillCount = 0;
        String finalArchetype = archetypesConfig.DEFAULT;

        for (ArchetypeConfig archetype : archetypesConfig.ARCHETYPES) {
            Set<String> archetypeSkills = new HashSet<>(archetype.SKILLS);
            archetypeSkills.retainAll(skills.stream().map(Skill::getId).collect(Collectors.toList()));

            if (archetypeSkills.size() > skillCount) {
                skillCount = archetypeSkills.size();
                finalArchetype = archetype.NAME;
            }
        }

        return finalArchetype;
    }

    public void checkTreeOnLogin(Player player) {
        Character character = characterService.getOrCreateCharacter(player);

        if (!skillGraphService.isPathValid(character.getSkills())) {
            characterService.resetCharacter(character);
            characterService.addSkill(character, skillGraphService.getSkillGraphRoot());
            rpgMsg.info((Audience)player, "The server's skill tree has changed. Your attributes and skill tree have been reset.");
        }
    }

    public String chooseSkillWithoutThrowing(Player player, String skillId) {
        try {
            return chooseSkill(player, skillId);
        } catch (RPGCommandException e) {
            player.sendMessage(e.toString());
        }
        return "";
    }

    public String chooseSkill(Player player, String skillId) throws RPGCommandException {
        Character character = characterService.getOrCreateCharacter(player);
        checkClass(character);

        Skill skill = getSkillById(skillId);

        if (character.getSkills().contains(skill)) {
            throw new RPGCommandException("You already have the skill ", skill.getName(), ".");
        }

        Set<Skill> skills = skillGraphService.getUniqueSkills();
        if (skillGraphService.getUniqueSkills().contains(skill)) {
            for (Skill rpgSkill : skills) {
                if (character.getSkills().contains(rpgSkill)) {
                    throw new RPGCommandException("You do not have access to the skill ", skill.getName(), ".");
                }
            }
        }

        double cost = skillGraphService.getCostForSkill(character, skill, character.getSkills()).orElseThrow(() -> {
            return new RPGCommandException("You do not have access to the skill ", skill.getName(), ".");
        });

        if (character.getUpgradeExperience() >= cost)  {
            if (character.getSpentUpgradeExperience() + cost > config.EXPERIENCE_SPENDING_LIMIT) {
                throw new RPGCommandException("You cannot go over the experience spending limit of ", config.EXPERIENCE_SPENDING_LIMIT, ".");
            }

            if (character.getSpentSkillUpgradeExperience() + cost > config.SKILL_SPENDING_LIMIT) {
                throw new RPGCommandException("You cannot go over the skill spending limit of ", config.SKILL_SPENDING_LIMIT, ".");
            }

            characterService.addSkill(character, skill);
            characterService.removeUpgradeExperience(character, cost);
            characterService.addSpentSkillUpgradeExperience(character, cost);

            // call event
            Bukkit.getPluginManager().callEvent(new SkillEvent(player, skill));

            rpgMsg.info((Audience)player, "You have unlocked the skill " + ChatColor.DARK_AQUA + skill.getName() + ".");
        } else {
            throw new RPGCommandException("You do not have enough experience to unlock that skill.");
        }

        // create command run
        return "";
    }

    public void removeSkill(Player player, String skillId) throws RPGCommandException {
        Skill skill = getSkillById(skillId);
        Character character = characterService.getOrCreateCharacter(player);

        if(!character.getSkills().contains(skill))
            throw new RPGCommandException("you do not have the skill " + skillId + ".");

        List<Skill> skillsCopy = new ArrayList<>(character.getSkills());
        skillsCopy.remove(skill);

        if(skillGraphService.isPathValid(skillsCopy)) {
            characterService.removeSkill(character, skill);
            skillGraphService.getCostForSkill(character, skill, skillsCopy).ifPresent(cost -> {
                characterService.addUpgradeExperience(character, cost);
                characterService.addSpentSkillUpgradeExperience(character, -cost);
            });

            // call event
            Bukkit.getPluginManager().callEvent(new SkillEvent(player, skill));

            rpgMsg.info((Audience)player, "You have removed the skill " + ChatColor.RED + skill.getName() + ChatColor.DARK_GRAY + ".");
        } else {
            throw new RPGCommandException("You cannot remove that skill.");
        }
    }

    public void resetSkills(Player player) {
        characterService.resetCharacterSkills(characterService.getOrCreateCharacter(player));
        rpgMsg.info((Audience)player, "Your skills have been reset.");
    }

    private Skill getSkillById(String skillId) throws RPGCommandException {
        return skillFacade.getSkillById(skillId).orElseThrow(() -> {
            return new RPGCommandException("No skill with ID ", skillId, " found.");
        });
    }

    private boolean validateExperience(double experience) {
        if (experience < config.EXPERIENCE_MIN) {
            return false;
        }

        if (experience > config.EXPERIENCE_MAX) {
            return false;
        }

        return true;
    }

    public void resetCharacter(Player player) {
        characterService.resetCharacter(characterService.getOrCreateCharacter(player));
        rpgMsg.info((Audience)player, "Skills and attributes reset. Your experience has been returned to you.");
    }

    public void onResourceRegen(ResourceEvent.Regen event, Player player) {
//        double amount = characterService.calcResourceRegen(attributeFacade.getAllAttributes(player));
        double amount = 5.0;
        event.setRegenAmount(amount);
    }

    public void onDamage(EntityDamageEvent event, DamageCause cause, LivingEntity target) {
        // Do I need to add indirect damage or just account for that in the else
        if(config.ENVIRONMENTAL_CALCULATIONS.containsKey(cause)) {
            // on environment damage
            onEnvironmentalDamage(event, cause, target);
        } else {
            onDirectDamage((EntityDamageByEntityEvent) event, cause);
        }
    }

    public void onEnvironmentalDamage(EntityDamageEvent event, DamageCause type, LivingEntity target) {
        //Remove damage modifiers
        resetDamageEvent(event);

        double damage;

        if (type == DamageCause.FALL) {
            Expression expression = expressionService.getExpression(config.ENVIRONMENTAL_CALCULATIONS.get(type));
            // grab fall distance
//            float blocksFallen = event.getEntity().get(Keys.FALL_DISTANCE).get();
            float blocksFallen = 0;

            expression.setVariable("DISTANCE", BigDecimal.valueOf(blocksFallen));
            damage = expressionService.evalExpression(target, expression).doubleValue();
        } else {
            damage = expressionService.evalExpression(target, config.ENVIRONMENTAL_CALCULATIONS.get(type)).doubleValue();
        }

        event.setDamage(damage);
    }

    public void onDirectDamage(EntityDamageByEntityEvent event, DamageCause cause) {
        Entity source = event.getDamager();
        Entity target = event.getEntity();

        Material weapon = Material.AIR;
        // get

//        Optional<DamageExpressionData> damageExpressionData = cause.get

        // If damage source is VOID, skip damage calculations this is true damage
        if(cause == DamageCause.VOID) {
            return;
        }

        if(cause == DamageCause.MAGIC) {

        }


    }

    private void resetDamageEvent(EntityDamageEvent event) {
//        event.get
    }

    public void onPlayerJoin(Player player) {
        Character character = characterService.getOrCreateCharacter(player);
        boolean fill = false;

        if(!character.isHasJoined()) {
            character.setHasJoined(true);
            fill = true;
        }

        updateCharacter(player, fill);
        checkTreeOnLogin(player);
    }

    public void onPlayerRespawn(Player player) {
        updateCharacter(player, true);
    }

    public void updateCharacter(LivingEntity living, boolean fill) {
        characterService.assignEntityHealthLimit(living, fill);
        characterService.assignEntityResourceLimit(living, fill);
        if (living instanceof Player) {
            characterService.assignEntityMovementSpeed(living);
        }
    }

    public void setKeepInventoryOnPVP(EntityDeathEvent event) {
            if(event instanceof PlayerDeathEvent playerDeath)
                playerDeath.setKeepInventory(config.PLAYERS_KEEP_INVENTORY_ON_PVP);
    }

}