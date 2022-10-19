package com.outcast.rpgclass.repositroy;

import com.google.inject.Singleton;
import com.outcast.rpgclass.character.Character;
import com.outcast.rpgcore.db.CachedRepository;

import java.util.UUID;

@Singleton
public class CharacterRepository extends CachedRepository<Character, UUID> {
    public CharacterRepository() {
        super(Character.class);
    }
}
