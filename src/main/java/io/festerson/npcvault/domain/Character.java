package io.festerson.npcvault.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="characters")
public class Character {

    @Id
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("race")
    private CharacterRace crace;
    @JsonProperty("class")
    private CharacterClass cclass;
    @JsonProperty("level")
    private int level;
    @JsonProperty("str")
    private int strength;
    @JsonProperty("dex")
    private int dexterity;
    @JsonProperty("con")
    private int constitution;
    @JsonProperty("int")
    private int intelligence;
    @JsonProperty("wis")
    private int wisdom;
    @JsonProperty("cha")
    private int charisma;
    @JsonProperty("ac")
    private int ac;
    @JsonProperty("hp")
    private int hp;
    @JsonProperty("cr")
    private int cr;
    @JsonProperty("type")
    private CharacterType ctype;
    @JsonProperty("img")
    private String imageUrl;
    @JsonProperty("player")
    private String playerId;

    public Character(){}

    public Character(
            String name,
            CharacterRace crace,
            CharacterClass cclass,
            int level,
            int strength,
            int dexterity,
            int constitution,
            int intelligence,
            int wisdom,
            int charisma,
            int ac,
            int hp,
            int cr,
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
        this.cr = cr;
        this.ctype = ctype;
        this.imageUrl = imageUrl;
        this.playerId = playerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacterRace getCrace() {
        return crace;
    }

    public void setCrace(CharacterRace crace) {
        this.crace = crace;
    }

    public CharacterClass getCclass() {
        return cclass;
    }

    public void setCclass(CharacterClass cclass) {
        this.cclass = cclass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstituion() {
        return constitution;
    }

    public void setConstituion(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCr() {
        return cr;
    }

    public void setCr(int cr) {
        this.cr = cr;
    }

    public CharacterType getCtype() {
        return ctype;
    }

    public void setCtype(CharacterType ctype) {
        this.ctype = ctype;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String player_id) {
        this.playerId = player_id;
    }
}
