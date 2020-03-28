package io.festerson.rpgvault.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection="players")
public class Player {

    @Id
    private String id;

    @NotBlank
    @JsonProperty("name")
    private String name;

    @Email
    @NotNull
    @JsonProperty("email")
    private String email;

    @JsonProperty("img")
    private String imageUrl;

    public Player(
            String name,
            String email,
            String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
