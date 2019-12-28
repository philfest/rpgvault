package io.festerson.npcvault.repository;

import io.festerson.npcvault.domain.Character;
import io.festerson.npcvault.domain.CharacterClass;
import io.festerson.npcvault.domain.CharacterRace;
import io.festerson.npcvault.domain.CharacterType;
import io.festerson.npcvault.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CharacterRepositoryTest {

    @Autowired
    CharacterRepository repository;

    @Autowired
    ReactiveMongoOperations operations;

    @Before
    public void setUp() throws Exception{
        operations.collectionExists(Character.class)
                .flatMap(exists -> exists ? operations.dropCollection(Character.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Character.class, new CollectionOptions(1024L * 1024L, 100L, true)))
                .then()
                .block();
        repository
                .saveAll(Flux.just(
                        TestUtils.generateCharacter("Test Character"),
                        TestUtils.generateCharacter(null),
                        TestUtils.generateCharacter(null))).then().block();
    }

    @Test
    public void getCharacterByNameTest() {
        Character character = repository.findByName("Test Character").block();
        assertThat(character).isNotNull();
    }




}
