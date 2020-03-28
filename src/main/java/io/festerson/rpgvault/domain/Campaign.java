package io.festerson.rpgvault.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection="campaigns")
public class Campaign {

    @Id
    private String id;

    @NotBlank
    private String name;

    private Date startDate;

    private Date endDate;

    @JsonProperty("characters")
    private List<String> characterIds;

    @JsonProperty("npcs")
    private List<String> npcIds;

    @JsonProperty("monsters")
    private List<String> monsterIds;

    @JsonProperty("players")
    private List<String> playerIds;

    @NotBlank
    @JsonProperty("dm")
    private String dmId;

    private String description;

    private String imageUrl;

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
}
