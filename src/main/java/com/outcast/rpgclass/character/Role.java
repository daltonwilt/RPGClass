package com.outcast.rpgclass.character;

import com.outcast.rpgclass.api.skill.Skill;

import java.util.Collections;
import java.util.Set;

//===========================================================================================================
// Object that stores data for a specific class
//===========================================================================================================

public class Role {

    public static final Role adventurer = new Role("Adventurer", Collections.emptySet(), "You have not acquired any skills.");
    private String name;
    private Set<Skill> skills;
    private String description;

    public Role(String name, Set<Skill> skills, String description) {
        this.name = name;
        this.skills = skills;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
