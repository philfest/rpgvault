package io.festerson.rpgvault.repository;

import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.domain.CharacterClass;
import io.festerson.rpgvault.domain.CharacterRace;
import io.festerson.rpgvault.domain.CharacterType;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CharacterRepositoryTest {

    @Autowired
    CharacterRepository repository;

    @Autowired
    ReactiveMongoOperations operations;

    @BeforeEach
    public void setUp() {
        operations.collectionExists(Character.class)
                .flatMap(exists -> exists ? operations.dropCollection(Character.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Character.class, new CollectionOptions(1024L * 1024L, 100L, true)))
                .then()
                .block();
        repository
                .saveAll(Flux.just(
                        TestUtils.generateCharacter("Test Character","ptest"),
                        TestUtils.generateCharacter(null, "ptest"),
                        TestUtils.generateCharacter(null, null),
                        TestUtils.generateCharacter(null, null))).then().block();
    }

    @Test
    public void getCharacterByName() {
        Character character = repository.findByName("Test Character").block();
        assertThat(character).isNotNull();
    }

    @Test
    public void getCharactersByPlayerId() {

        List<Character> campaigns = repository.getCharactersByPlayerId("ptest").collectList().block();
        assertThat(campaigns).hasSize(2);
    }

    @Test
    public void getAllCharacters() {

        List<Character> ids = repository.findAll().collectList().block();
        assertThat(ids).hasSize(4);
    }

}
