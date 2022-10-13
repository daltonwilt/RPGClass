package com.outcast.rpgclass.facade;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.outcast.rpgclass.api.skill.DescriptionArgument;
import com.outcast.rpgclass.api.skill.Skill;
import com.outcast.rpgclass.api.skill.TargetedSkill;
import com.outcast.rpgclass.character.Character;
import com.outcast.rpgclass.config.RPGClassConfig;
import com.outcast.rpgclass.service.ExpressionService;
import com.outcast.rpgclass.service.CharacterService;
import com.outcast.rpgclass.service.SkillGraphService;
import com.outcast.rpgcore.util.TextUtil;
import com.outcast.rpgskill.RPGSkill;
import com.outcast.rpgskill.api.event.SkillCastEvent;
import com.udojava.evalex.Expression;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

@Singleton
public class SkillFacade {

    @Inject
    private ExpressionService expressionService;

    @Inject
    private CharacterService characterService;

    @Inject
    private AttributeFacade attributeFacade;

    @Inject
    private SkillGraphService skillGraphService;

    @Inject
    private RPGClassConfig config;

    @Inject
    private RPGMessagingFacade rpgMsg;

    public void sendMessageOnSkillCast(SkillCastEvent.Post event) {
        TextComponent message;

        if (!event.getResult().getMessage().equals(Component.empty().toString())) {
            message = Component.text(event.getResult().getMessage());
        } else {
            String name = event.getEntity().getName();
            message = rpgMsg.formatInfo(ChatColor.DARK_AQUA + name + ChatColor.DARK_GRAY + " cast " + ChatColor.DARK_RED + event.getSkill().getName());
        }

        event.getEntity().getNearbyEntities(config.SKILL_MESSAGE_DISTANCE, config.SKILL_MESSAGE_DISTANCE, config.SKILL_MESSAGE_DISTANCE).forEach(entity -> {
            if (entity instanceof Player) {
                ((Audience)entity).sendMessage(message);
            }
        });
    }

    public TextComponent renderAvailableSkill(Skill skill, Player source, boolean showCost) {
        TextComponent.Builder skillText = renderSkill(skill, source).toBuilder();

        Set<Skill> linkedSkills = skillGraphService.getLinkedSkills(Collections.singletonList(skill));
        if (linkedSkills.isEmpty()) {
            return skillText.build();
        }

        // get the components that are hover actions
        TextComponent.Builder hoverText = skillText;

        List<Skill> skills = characterService.getOrCreateCharacter(source).getSkills();
        if (showCost) {
            Character character = characterService.getOrCreateCharacter(source);
            hoverText.append(Component.text("\n" + "\n" + ChatColor.DARK_GRAY + "EXP to Unlock: " + ChatColor.DARK_GREEN + skillGraphService.getCostForSkill(character, skill, skills).get()));
        }
        hoverText.append(Component.text("\n" + ChatColor.DARK_GRAY + "Next Skills: "));

        int index = 0;
        for (Skill linkedSkill : linkedSkills) {
            index++;
            hoverText.append(renderSkill(linkedSkill, source));
            if (index < linkedSkills.size()) {
                hoverText.append(Component.text(ChatColor.GOLD + ", "));
            }
        }
        skillText.hoverEvent(HoverEvent.showText(hoverText.build()));

        return skillText.build();
    }

    public TextComponent renderSkill(Skill skill, Player source) {
        TextComponent.Builder hoverText = Component.text();
        hoverText.append(Component.text(ChatColor.GOLD + skill.getName() + "\n"));
        hoverText.append(Component.text(skill.getDescription(source) + "\n"));
        hoverText.append(Component.text("\n" +  ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.DARK_GREEN + TextUtil.formatDuration(skill.getCooldown(source))));
        hoverText.append(Component.text("\n" + ChatColor.DARK_GRAY + "Energy Cost: " + ChatColor.DARK_GREEN + (int) skill.getResourceCost(source)));

        if (skill instanceof TargetedSkill) {
            int range = (int) ((TargetedSkill) skill).getRange(source);
            hoverText.append(Component.text("\n" + ChatColor.DARK_GRAY + "Range: " + ChatColor.DARK_GREEN + range + " blocks"));
        }

        return Component.text("")
        .append(Component.text(ChatColor.GOLD + skill.getName()))
        .hoverEvent(HoverEvent.showText(hoverText.build()));
    }

    @SafeVarargs
    public final TextComponent renderSkillDescription(LivingEntity living, TextComponent description, Map.Entry<String, ?>... descriptionArguments) {
        if (descriptionArguments == null) {
            return description;
        }

        Map<String, TextComponent> renderedArguments = new HashMap<>();
        for (Map.Entry<String, ?> argument : descriptionArguments) {

            // by default, the value of the argument is the Text of the argument value
            TextComponent value = Component.text(argument.getValue().toString());

            // if the argument is of type DescriptionArgument, pass in the living as the source and evaluate
            if (argument.getValue() instanceof DescriptionArgument) {
                value = Component.text(((DescriptionArgument) argument.getValue()).apply(living).toString());
            }

            // if the argument is of type Expression, evaluate it with the user as source
            if (argument.getValue() instanceof Expression) {
                value = Component.text(expressionService.evalExpression(living, (Expression) argument.getValue()).toString());
            }
            // put the rendered argument into the map
            renderedArguments.put(argument.getKey(), value);
        }

        // Apply all rendered arguments to the template and convert to text
        TextComponent.Builder finalDescription = Component.text();
        finalDescription.append(description);
        renderedArguments.forEach((k, v) -> finalDescription.append(v));

        return finalDescription.build();
    }

    public long getSkillCooldown(LivingEntity living, String cooldownExpression) {
        return expressionService.evalExpression(living, cooldownExpression).longValue();
    }

    public double getSkillResourceCost(LivingEntity living, String resourceCostExpression) {
        return expressionService.evalExpression(living, resourceCostExpression).doubleValue();
    }

    public Optional<Skill> getSkillById(String skillId) {
        return RPGSkill.getInstance().getSkillService().getSkillById(skillId)
                .filter(skill -> skill instanceof Skill)
                .map(skill -> (Skill) skill);
    }

}
