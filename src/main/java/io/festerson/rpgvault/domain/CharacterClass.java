package io.festerson.rpgvault.domain;

import java.util.Random;

public enum CharacterClass {

    BARBARIAN  ("Barbarian"),
    BARD  ("Bard"),
    CLERIC ("Cleric"),
    DRUID("Druid"),
    FIGHTER("Fighter"),
    MONK("Monk"),
    PALADIN("Paladin"),
    RANGER("Ranger"),
    ROGUE("Rogue"),
    SORCERER("Sorcerer"),
    WARLOCK("Warlock"),
    WIZARD("Wizard");

    private final String name;

    CharacterClass(String name){
        this.name = name;
    }

    public static final CharacterClass pickOne(){
        Random RND = new Random();
        CharacterClass values[] = CharacterClass.values();
        return values[RND.nextInt(values.length)];
    }
}
