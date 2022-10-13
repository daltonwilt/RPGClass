package com.outcast.rpgclass.service;

import com.google.inject.Inject;
import com.outcast.rpgclass.api.character.RPGCharacter;
import com.outcast.rpgclass.api.stat.AttributeType;
import com.outcast.rpgclass.api.stat.AttributeTypeRegistry;
import com.outcast.rpgclass.config.RPGClassConfig;
import com.outcast.rpgclass.config.stat.AttributesConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.outcast.rpgcore.util.InventoryUtil.*;

//===========================================================================================================
// Implement this system after mob integration is finished.
//===========================================================================================================
public class AttributeService {

    private Map<AttributeType, Double> defaultAttributes;

    @Inject
    private RPGClassConfig config;

    @Inject
    private AttributesConfig attributesConfig;

    @Inject
    private AttributeTypeRegistry attributeTypeRegistry;

    @Inject
    private CharacterService characterService;

    public AttributeService() {}

    /**
     * @return Rreturns a map containing the default values configured for each attribute
     */
    public Map<AttributeType, Double> getDefaultAttributes() {
        if (defaultAttributes == null) {
            defaultAttributes = new HashMap<>();

            attributeTypeRegistry.getAll().forEach( type -> defaultAttributes.put(type, Math.max(0, type.getDefaultValue())));

        }
        return defaultAttributes;
    }

    /**
     * Set any missing attributes to 0
     * @param attributes Attribute hashmap to modify
     * @return modified attributes with missing attributes set to 0
     */
    public Map<AttributeType, Double> fillInAttributes(Map<AttributeType, Double> attributes) {
        for (AttributeType type : attributeTypeRegistry.getAll()) {
            attributes.putIfAbsent(type, 0.0);
        }
        return attributes;
    }

    /**
     * @param item that is being checked Lore
     * @return data map from the material parameter passed
     */
    public Map<AttributeType, Double> getItemStackAttributes(ItemStack item) {
        // get attribute data from item stack Lore()
        return null;
    }

    //===========================================================================================================
    // Getting data from each slot of equipable materials.
    //===========================================================================================================

    public Map<AttributeType, Double> getOffhandAttributes(Entity entity) {
        return getOffHand(entity).map(itemStack -> config.OFFHAND_ITEMS.contains(itemStack.getType()) ? getItemStackAttributes(itemStack) : null).orElse(new HashMap<>());
    }

    public Map<AttributeType, Double> getMainHandAttributes(Entity entity) {
        return getMainHand(entity).map(itemStack -> config.MAINHAND_ITEMS.contains(itemStack.getType()) ? getItemStackAttributes(itemStack) : null).orElse(new HashMap<>());
    }

    public Map<AttributeType, Double> getHelmetAttributes(Entity entity) {
        return getHead(entity).map(this::getItemStackAttributes).orElse(new HashMap<>());
    }

    public Map<AttributeType, Double> getChestplateAttributes(Entity entity) {
        return getChest(entity).map(this::getItemStackAttributes).orElse(new HashMap<>());
    }

    public Map<AttributeType, Double> getLeggingsAttributes(Entity entity) {
        return getLegs(entity).map(this::getItemStackAttributes).orElse(new HashMap<>());
    }

    public Map<AttributeType, Double> getBootsAttributes(Entity entity) {
        return getBoots(entity).map(this::getItemStackAttributes).orElse(new HashMap<>());
    }

    public Map<AttributeType, Double> getAttributes(Entity entity) {
        Map<AttributeType, Double> result = new HashMap<>();

        mergeAttributes(result, getHelmetAttributes(entity));
        mergeAttributes(result, getChestplateAttributes(entity));
        mergeAttributes(result, getLeggingsAttributes(entity));
        mergeAttributes(result, getBootsAttributes(entity));
        mergeAttributes(result, getMainHandAttributes(entity));
        mergeAttributes(result, getOffhandAttributes(entity));

        return result;
    }

    /**
     * Merge the values of the two attribute type maps.<br>
     * WARNING: This will ALTER the source map
     *
     * @param source     The map to be altered
     * @param additional The additional attributes to be added
     * @return The altered source map
     */
    private Map<AttributeType, Double> mergeAttributes(Map<AttributeType, Double> source, Map<AttributeType, Double> additional) {
        additional.forEach((type, value) -> source.merge(type, value, Double::sum));
        return additional;
    }

    /**
     * @param character that we are checking for external attributes
     * @return the external attributes coming from sources such as buffs/skills/etc...
     */
    public Map<AttributeType, Double> getExternalAttributes(RPGCharacter<?> character) {
        return new HashMap<>(character.getExternalAttributes());
    }

    /**
     * @param entity that we are retrieving all attributes for external/equipped/character
     * @return attribute data map for all possible attributes
     */
    public Map<AttributeType, Double> getAllAttributes(Entity entity) {
        RPGCharacter<?> character = characterService.getOrCreateCharacter(entity);

        HashMap<AttributeType, Double> results = new HashMap<>();

        if (entity instanceof Player) {
            mergeAttributes(results, getDefaultAttributes());
        }

        mergeAttributes(results, character.getCharacterAttributes());
        mergeAttributes(results, getEquipmentAttributes(entity));
        mergeAttributes(results, character.getExternalAttributes());

        results.replaceAll((key, value) -> Math.max(0.0d, value));
        return results;
    }

    /**
     * @param entity checking materials from
     * @return attribute data from equipped materials
     */
    public Map<AttributeType, Double> getEquipmentAttributes(Entity entity) {
        Map<AttributeType, Double> result = new HashMap<>();
        mergeAttributes(result, getAttributes(entity));
        return result;
    }

    public double getUpgradeableAttributeCount(RPGCharacter<?> character) {
//        return Sponge.getRegistry().getAllOf(AttributeType.class).stream()
//                .filter(AttributeType::isUpgradable)
//                .map(attributeType -> character.getCharacterAttributes().getOrDefault(attributeType, 0.0))
//                .reduce(Double::sum)
//                .orElse(0.0);
        return 0.0;
    }

}
