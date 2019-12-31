package io.festerson.rpgvault.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;

@Getter
@Setter
@NoArgsConstructor
@Document(collection="characters")
public class Character {

    @Id
    private String id;
    private String name;
    @JsonProperty("race")
    private CharacterRace crace;
    @JsonProperty("class")
    private CharacterClass cclass;
    private Integer level;
    @JsonProperty("str")
    private Integer strength;
    @JsonProperty("dex")
    @Max(5)
    private Integer dexterity;
    @JsonProperty("con")
    private Integer constitution;
    @JsonProperty("int")
    private Integer intelligence;
    @JsonProperty("wis")
    private Integer wisdom;
    @JsonProperty("cha")
    private Integer charisma;
    private Integer ac;
    private Integer hp;
    @JsonProperty("type")
    private CharacterType ctype;
    private String imageUrl;
    @JsonProperty("player")
    private String playerId;

   public Character(
            String name,
            CharacterRace crace,
            CharacterClass cclass,
            Integer level,
            Integer strength,
            Integer dexterity,
            Integer constitution,
            Integer intelligence,
            Integer wisdom,
            Integer charisma,
            Integer ac,
            Integer hp,
            CharacterType ctype,
            String imageUrl,
            String playerId) {
        this.name = name;
        this.crace = crace;
        this.cclass = cclass;
        this.level = level;
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
        this.ac = ac;
        this.hp = hp;
        this.ctype = ctype;
        this.imageUrl = imageUrl;
        this.playerId = playerId;
    }
}
