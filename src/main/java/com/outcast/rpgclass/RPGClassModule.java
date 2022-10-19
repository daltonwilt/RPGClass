package com.outcast.rpgclass;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.outcast.rpgclass.config.RPGClassConfig;
import com.outcast.rpgclass.config.RPGCombatConfig;
import com.outcast.rpgclass.config.mob.MobsConfig;
import com.outcast.rpgclass.config.skill.SkillGraphConfig;
import com.outcast.rpgclass.facade.AttributeFacade;
import com.outcast.rpgclass.facade.CharacterFacade;
import com.outcast.rpgclass.facade.MobFacade;
import com.outcast.rpgclass.facade.RPGMessagingFacade;
import com.outcast.rpgclass.repositroy.CharacterRepository;
import com.outcast.rpgclass.service.AttributeService;
import com.outcast.rpgclass.service.DamageService;
import com.outcast.rpgclass.service.ExpressionService;
import com.outcast.rpgclass.service.CharacterService;

public class RPGClassModule extends AbstractModule {
    @Override
    protected void configure() {
        // Configs
        bind(RPGClassConfig.class).in(Scopes.SINGLETON);
        bind(RPGCombatConfig.class).in(Scopes.SINGLETON);
        bind(SkillGraphConfig.class).in(Scopes.SINGLETON);
        bind(MobsConfig.class).in(Scopes.SINGLETON);

        // Repository
//        bind(CharacterRepository.class).in(Scopes.SINGLETON);

        // Services
        bind(ExpressionService.class).in(Scopes.SINGLETON);
        bind(AttributeService.class).in(Scopes.SINGLETON);
        bind(DamageService.class).in(Scopes.SINGLETON);
        bind(CharacterService.class).in(Scopes.SINGLETON);

        // Facades
        bind(RPGMessagingFacade.class).in(Scopes.SINGLETON);
        bind(CharacterFacade.class).in(Scopes.SINGLETON);
        bind(AttributeFacade.class).in(Scopes.SINGLETON);
        bind(MobFacade.class).in(Scopes.SINGLETON);

        // Listeners
//        bind(EntityListener.class).in(Scopes.SINGLETON);
//        bind(SkillsListener.class).in(Scopes.SINGLETON);
    }
}
