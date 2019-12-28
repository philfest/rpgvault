package io.festerson.rpgvault.domain;

import java.util.Random;

public enum CharacterRace {

    DWARF  ("Dwarf"),
    ELF  ("Elf"),
    HALFLING ("Halfling"),
    HUMAN("Human"),
    DRAGONBORN("Dragonborn"),
    GNOME("Gnome"),
    HALF_ELF("Half-elf"),
    HALF_ORC("Half-orc"),
    TIEFLING("Tiefling");

    private final String name;

    CharacterRace(String name){
        this.name = name;
    }

    public static final CharacterRace pickOne() {
        Random RND = new Random();
        CharacterRace values[] = CharacterRace.values();
        return values[RND.nextInt(values.length)];
    }
}
