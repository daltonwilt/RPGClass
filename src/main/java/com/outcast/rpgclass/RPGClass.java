package com.outcast.rpgclass;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.outcast.rpgclass.command.CommandClass;
import com.outcast.rpgclass.config.RPGClassConfig;
import com.outcast.rpgclass.config.RPGCombatConfig;
import com.outcast.rpgclass.config.archetype.ArchetypesConfig;
import com.outcast.rpgclass.config.loot.ItemTemplatesConfig;
import com.outcast.rpgclass.config.mob.MobsConfig;
import com.outcast.rpgclass.config.skill.SkillGraphConfig;
import com.outcast.rpgclass.config.stat.AttributesConfig;
import com.outcast.rpgclass.facade.*;
import com.outcast.rpgclass.listener.EntityListener;
import com.outcast.rpgclass.listener.SkillsListener;
import com.outcast.rpgclass.repositroy.CharacterRepository;
import com.outcast.rpgclass.service.*;
import com.outcast.rpgcore.RPGCore;
import com.outcast.rpgcore.command.CommandService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RPGClass extends JavaPlugin {

    private static RPGClass instance;
    private static Components components;

    @Inject
    private Injector injector;

    //===========================================================================================================
    // Getter methods.
    //===========================================================================================================

    public static RPGClass getInstance() {
        return instance;
    }

    public static RPGClassConfig getClassConfig() {
        return components.classConfig;
    }

    public static RPGCombatConfig getCombatConfig() {
        return components.combatConfig;
    }

    public AttributesConfig getAttributesConfig() {
        return components.attributesConfig;
    }

    public SkillGraphConfig getGraphConfig() {
        return components.skillGraphConfig;
    }

    public MobsConfig getMobsConfig() {
        return components.mobsConfig;
    }

    public ArchetypesConfig getArchetypesConfig() {
        return components.archetypesConfig;
    }

    public ItemTemplatesConfig getTemplatesConfig() {
        return components.itemTemplatesConfig;
    }

    public CharacterRepository getCharacterRepository() { return components.characterRepository; }

    public AttributeService getAttributeService() { return components.attributeService; }

    public CharacterService getCharacterService() {
        return components.characterService;
    }

    public SkillGraphService getSkillGraphService() {
        return components.skillGraphService;
    }

    public MobService getMobService() {
        return components.mobService;
    }

    public ExpressionService getExpressionService() {
        return components.expressionService;
    }

    public DamageService getDamageService() {
        return components.damageService;
    }

    public RPGMessagingFacade getRPGMessagingFacade() {
        return components.rpgMessagingFacade;
    }

    public AttributeFacade getAttributeFacade() {
        return components.attributeFacade;
    }

    public CharacterFacade getCharacterFacade() {
        return components.characterFacade;
    }

    public SkillFacade getSkillFacade() {
        return components.skillFacade;
    }

    public ItemFacade getItemFacade() {
        return components.itemFacade;
    }

    public MobFacade getMobFacade() {
        return components.mobFacade;
    }


    //===========================================================================================================
    // Utility methods and components.
    //===========================================================================================================

    private static class Components {
        @Inject
        RPGClassConfig classConfig;

        @Inject
        RPGCombatConfig combatConfig;

        @Inject
        AttributesConfig attributesConfig;

        @Inject
        SkillGraphConfig skillGraphConfig;

        @Inject
        MobsConfig mobsConfig;

        @Inject
        ArchetypesConfig archetypesConfig;

        @Inject
        ItemTemplatesConfig itemTemplatesConfig;

        @Inject
        CharacterRepository characterRepository;

        @Inject
        AttributeService attributeService;

        @Inject
        CharacterService characterService;

        @Inject
        SkillGraphService skillGraphService;

        @Inject
        MobService mobService;

        @Inject
        ExpressionService expressionService;

        @Inject
        DamageService damageService;

        @Inject
        HealingService healingService;

        @Inject
        RPGMessagingFacade rpgMessagingFacade;

        @Inject
        AttributeFacade attributeFacade;

        @Inject
        CharacterFacade characterFacade;

        @Inject
        SkillFacade skillFacade;

        @Inject
        ItemFacade itemFacade;

        @Inject
        MobFacade mobFacade;

        @Inject
        EntityListener entityListener;

        @Inject
        SkillsListener skillsListener;
    }

    private static void printBlank() {
        RPGCore.info(instance, "");
    }

    private static void printDivider() {
        RPGCore.info(instance, "================================================================================================");
    }

    //===========================================================================================================
    // onEnable onDisable methods.
    //===========================================================================================================

    @Override
    public void onEnable() {
        instance = this;

        printDivider();
        printBlank();
        RPGCore.info(instance, "  RPGClass v%s", getDescription().getVersion());
        RPGCore.info(instance, "  You're running on %s.", getServer().getVersion());
        printDivider();

        // Register command manager
        try {
            CommandService.createCommand(this, "class", "Command prefix for RPGClass.", "/class", CommandClass.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Inject Modules
        components = new Components();
        injector = Guice.createInjector(new RPGClassModule());
        injector.injectMembers(components);

        // Initialize Configuration
        getClassConfig().init();
        getCombatConfig().init();
        getGraphConfig().init();
        getMobsConfig().init();
        getArchetypesConfig().init();
        getTemplatesConfig().init();
        getAttributesConfig().init();

        // Initialize Services
        getCharacterService().init();
        getExpressionService().init();

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(components.entityListener, this);
        Bukkit.getPluginManager().registerEvents(components.skillsListener, this);
    }

    @Override
    public void onDisable() {
        // flush character repository
        RPGCore.info(instance, "RPGClass v%s is being disabled.", getDescription().getVersion());
    }

}
