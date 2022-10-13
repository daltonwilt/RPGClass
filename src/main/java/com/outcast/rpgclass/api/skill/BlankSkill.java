package com.outcast.rpgclass.api.skill;

import com.outcast.rpgskill.api.exception.CastException;
import com.outcast.rpgskill.api.skill.CastResult;
import org.bukkit.entity.LivingEntity;

//===========================================================================================================
// Placeholder skill if a skill isn't found
//===========================================================================================================
public final class BlankSkill extends Skill {

    public static final BlankSkill blank = new BlankSkill();

    private BlankSkill() {
        super(SkillSpec.create()
                .cooldown("0")
                .id("blank")
                .name("Blank")
                .resourceCost("0")
        );
    }

    @Override
    public CastResult cast(LivingEntity living, long l, String... strings) throws CastException {
        return CastResult.empty();
    }

}
