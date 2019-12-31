package io.festerson.rpgvault.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@Document(collection="characters")
public class Character {

    @Id
    private String id;

    @NotBlank(message = "Every character must have a name!!!!!")
    private String name;

    @NotNull
    @JsonProperty("race")
    private CharacterRace crace;

    @NotNull
    @JsonProperty("class")
    private CharacterClass cclass;

    @Min(1)
    private Integer level;

    @Min(3)
    @JsonProperty("str")
    private Integer strength;

    @Min(3)
    @JsonProperty("dex")
    private Integer dexterity;

    @Min(3)
    @JsonProperty("con")
    private Integer constitution;

    @Min(3)
    @JsonProperty("int")
    private Integer intelligence;

    @Min(3)
    @JsonProperty("wis")
    private Integer wisdom;

    @Min(3)
    @JsonProperty("cha")
    private Integer charisma;

    @Min(10)
    private Integer ac;

    @Min(1)
    private Integer hp;

    @NotNull
    @JsonProperty("type")
    private CharacterType ctype;

    private String imageUrl;

    @NotBlank
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
