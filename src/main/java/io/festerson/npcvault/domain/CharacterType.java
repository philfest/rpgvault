package io.festerson.npcvault.domain;

public enum CharacterType {

    PC  ("Character"),
    NPC  ("Non-player Character"),
    MONSTER ("Critter");

    private final String name;

    CharacterType(String name){
        this.name = name;
    }
}
