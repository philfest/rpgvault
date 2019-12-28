package io.festerson.npcvault.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection="campaigns")
public class Campaign {

    @Id
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("start")
    private Date startDate;
    @JsonProperty("end")
    private Date endDate;
    @JsonProperty("characters")
    private List<String> characterIds;
    @JsonProperty("npcs")
    private List<String> npcIds;
    @JsonProperty("monsters")
    private List<String> monsterIds;
    @JsonProperty("players")
    private List<String> playerIds;
    @JsonProperty("dm")
    private String dmId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("img")
    private String imageUrl;

    public Campaign(){}

    public Campaign(
            String name,
            Date startDate,
            Date endDate,
            List<String> playerIds,
            List<String> characterIds,
            List<String> npcIds,
            List<String> monsterIds,
            String dmId,
            String description,
            String imageUrl) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.playerIds = playerIds;
        this.characterIds = characterIds;
        this.npcIds = npcIds;
        this.monsterIds = monsterIds;
        this.dmId = dmId;
        this.description = description;
        this.imageUrl = imageUrl;
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

    public Date getStart_date() {
        return startDate;
    }

    public void setStart_date(Date start_date) {
        this.startDate = start_date;
    }

    public Date getEnd_date() {
        return endDate;
    }

    public void setEnd_date(Date end_date) {
        this.endDate = end_date;
    }

    public String getDm_id() {
        return dmId;
    }

    public void setDm_id(String dm_id) {
        this.dmId = dm_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }

    public List<String> getCharacterIds() {
        return characterIds;
    }

    public void setCharacterIds(List<String> characterIds) {
        this.characterIds = characterIds;
    }

    public List<String> getNpcIds() {
        return npcIds;
    }

    public void setNpcIds(List<String> npcIds) {
        this.npcIds = npcIds;
    }

    public List<String> getMonsterIds() {
        return monsterIds;
    }

    public void setMonsterIds(List<String> monsterIds) {
        this.monsterIds = monsterIds;
    }

    public List<String> getPlayerIds() { return playerIds; }

    public void setPlayerIds(List<String> playerIds) { this.playerIds = playerIds; }
}
