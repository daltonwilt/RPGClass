package com.outcast.rpgclass.facade;

import com.google.inject.Inject;
import com.outcast.rpgclass.api.character.RPGCharacter;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgclass.character.Character;
import com.outcast.rpgclass.command.exception.RPGCommandException;
import com.outcast.rpgclass.config.RPGClassConfig;
import com.outcast.rpgclass.config.stat.AttributesConfig;
import com.outcast.rpgclass.service.AttributeService;
import com.outcast.rpgclass.service.ExpressionService;
import com.outcast.rpgclass.service.CharacterService;
import com.udojava.evalex.Expression;
import net.kyori.adventure.audience.Audience;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Map;

import static com.outcast.rpgcore.util.InventoryUtil.getMainHand;

//===========================================================================================================
// Implement this system after mob integration is finished.
//===========================================================================================================

@Singleton
public class AttributeFacade {

    @Inject
    private RPGClassConfig config;

    @Inject
    private AttributesConfig attributesConfig;

    @Inject
    private AttributeService attributeService;

    @Inject
    private CharacterService characterService;

    @Inject
    private RPGMessagingFacade rpgMsg;

    @Inject
    private ExpressionService expressionService;

    public void init() {}

    /**
     * Adds a CharacterAttribute to a players character
     * @param player The Player to add the attribute for
     * @param attributeType The attribute to add
     * @param amount The amount to add
     * @throws RPGCommandException
     */
    public void addPlayerAttribute(Player player, AttributeType attributeType, double amount) throws RPGCommandException {
        Character character = characterService.getOrCreateCharacter(player);

        if (validateCharacterAttribute(attributeType, character.getCharacterAttributes().getOrDefault(attributeType, config.ATTRIBUTE_MIN) + amount)) {
            characterService.addAttribute(character, attributeType, amount);
        }
    }

    /**
     * Removes a CharacterAttribute from a players character
     * @param player The player to remove the attribute for
     * @param attributeType The Attribute to remove
     * @param amount The amount to remove
     * @throws RPGCommandException
     */
    public void removePlayerAttribute(Player player, AttributeType attributeType, double amount) throws RPGCommandException {
        Character character = characterService.getOrCreateCharacter(player);

        if (validateCharacterAttribute(attributeType, character.getCharacterAttributes().getOrDefault(attributeType, config.ATTRIBUTE_MIN) - amount)) {
            characterService.removeAttribute(character, attributeType, amount);
        }
    }

    /**
     * Validate setting the value of an Attribute against a Character
     * @param type The Attribute to be set
     * @param amount The amount to the attribute will be set to
     * @return true if this is a valid action
     * @throws RPGCommandException When the Attribute is invalid
     */
    private boolean validateCharacterAttribute(AttributeType type, Double amount) throws RPGCommandException {
        if (!type.isUpgradable()) {
            throw new RPGCommandException("Cannot set a Non-Upgradable attribute against a Character");
        }
        if (amount < config.ATTRIBUTE_MIN) {
            throw new RPGCommandException("A player cannot have attributes less than ", config.ATTRIBUTE_MIN);
        }

        if (amount > config.ATTRIBUTE_MAX) {
            throw new RPGCommandException("A player cannot have attributes bigger than ", config.ATTRIBUTE_MAX);
        }

        return true;
    }

    public void mergeExternalAttributes(RPGCharacter<?> rpgc, Map<AttributeType, Double> attributes) {
        characterService.mergeExternalAttributes(rpgc, attributes);
    }

    /**
     * Called when the player purchases an attribute for experience
     * @param player The Player purchasing the attribute
     * @param type The type of attribute being purchased
     * @param amount The amount being purchased
     * @throws RPGCommandException
     */
    public void purchaseAttribute(Player player, AttributeType type, double amount) throws RPGCommandException {
        Character character = characterService.getOrCreateCharacter(player);

        // if attribute is not upgradeable then cast exception
        if(!type.isUpgradable()) { throw new RPGCommandException("You may not purchase a Non-Upgradable attribute."); }

        // gather price for the attributes purchased
        double cost = getCostForAttributes(player, amount);

        // if the player has already reached their experience spending limit, cancel
        if(character.getSpentUpgradeExperience() + cost > config.EXPERIENCE_SPENDING_LIMIT) {
            throw new RPGCommandException("You cannot go over the experience spending limit of ", config.EXPERIENCE_SPENDING_LIMIT, ".");
        } else {
            // base case error checks
            if(character.getUpgradeExperience() - cost > config.EXPERIENCE_MIN) {
                throw new RPGCommandException("You cannot go over the attribute spending limit of ", config.ATTRIBUTE_SPENDING_LIMIT, ".");
            }

            if (character.getSpentAttributeUpgradeExperience() + cost > config.ATTRIBUTE_SPENDING_LIMIT) {
                throw new RPGCommandException(
                        "You cannot go over the attribute spending limit of ", config.ATTRIBUTE_SPENDING_LIMIT, "."
                );
            }

            double afterPurchase = character.getCharacterAttributes().getOrDefault(type, config.ATTRIBUTE_MIN);
            if(afterPurchase > config.ATTRIBUTE_MAX) {
                throw new RPGCommandException("You cannot have more than a base of ", config.ATTRIBUTE_MAX, " in this attribute.");
            }

            characterService.addAttribute(character, type, amount);
            characterService.removeUpgradeExperience(character, cost);
            characterService.addSpentAttributeUpgradeExperience(character, cost);

            rpgMsg.info((Audience)player,
                    "You have added "+ type.getColor() + (int) amount + " " +
                    type.getName() + ChatColor.DARK_GRAY +
                    " for " + ChatColor.DARK_GREEN + cost + ChatColor.DARK_GRAY +
                    " experience."
            );
        }
    }

    private double getCostForAttributes(Player player, double amount) {
        Character character = characterService.getOrCreateCharacter(player);

        double totalPurchased = attributeService.getUpgradeableAttributeCount(character);

        double cost = 0;
        Expression costExp = expressionService.getExpression(config.ATTRIBUTE_UPGRADE_COST);
        for(int i=0; i<amount; i++) {
            costExp.setVariable("ATTRIBUTES", BigDecimal.valueOf(totalPurchased));
            costExp.setVariable("SKILLS", BigDecimal.valueOf(character.getSkills().size()));
            cost += costExp.eval().doubleValue();
            totalPurchased++;
        }

        return cost;
    }

    public void resetPlayerAttributes(Player player) {
        characterService.resetCharacterAttributes(characterService.getOrCreateCharacter(player));
        rpgMsg.info((Audience)player, "Your attributes have been reset.");
    }

    public void enchantPlayerHeldItem(Player player, AttributeType attributeTyp, Double amount) throws RPGCommandException {
        ItemStack item = getMainHand(player).orElse(null);

        if (item == null) {
            throw new RPGCommandException("You must be holding an item in order to enchant it with an attribute.");
        }

//        setItemAttributeValue(item, attributeType, amount);
//        updateItemLore(item);
    }

    public Map<AttributeType, Double> getAllAttributes(Entity entity) {
        return attributeService.getAllAttributes(entity);
    }

}
