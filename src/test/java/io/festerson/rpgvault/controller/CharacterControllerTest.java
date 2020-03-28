package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterControllerTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    CharacterRepository characterRepository;

    @Test
    public void testGetCharactersHappyPath() throws Exception {
        Flux<Character> characters = TestUtils.buildCharacterRepositoryTestCollection();
        BDDMockito.given(characterRepository.findAll()).willReturn(characters);
        webTestClient
                .get().uri("/characters")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Character.class);
    }

    @Test
    public void testGetCharacterByIdHappyPath() throws Exception {
        Mono<Character> character = Mono.just(TestUtils.buildCharacter());
        BDDMockito.given(characterRepository.findById("1")).willReturn(character);
        webTestClient
                .get().uri("/characters/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Character.class);
    }


    @Test
    public void testGetCharactersByPlayerIdHappyPath() throws Exception {
        Flux<Character> characters = TestUtils.buildCharacterRepositoryTestCollection();
        BDDMockito.given(characterRepository.getCharactersByPlayerId("1")).willReturn(characters);
        webTestClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder
                            .path("/characters")
                            .queryParam("player", "1")
                            .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Character.class);
    }

    @Test
    public void testCreateCharacterHappyPath() throws Exception {
        Character character = TestUtils.buildCharacter();
        BDDMockito.given(characterRepository.save(any())).willReturn(Mono.just(character));
        webTestClient
                .post()
                .uri("/characters")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(character), Character.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Character.class);
    }

    @Test
    public void testUpdateCharacterHappyPath() throws Exception {
        Character character = TestUtils.buildCharacter();
        BDDMockito.given(characterRepository.save(character)).willReturn(Mono.just(character));
        BDDMockito.given(characterRepository.findById("1")).willReturn(Mono.just(character));
        webTestClient
                .put()
                .uri("/characters/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(character), Character.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Character.class);



/*        Campaign campaign = TestUtils.buildCampaign();
        BDDMockito.given(campaignRepository.save(campaign)).willReturn(Mono.just(campaign));
        BDDMockito.given(campaignRepository.findById("1")).willReturn(Mono.just(campaign));
        webTestClient
            .put()
            .uri("/campaigns/1")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(campaign), Campaign.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Campaign.class);*/
}

    @Test
    public void testDeleteCharacterHappyPath() throws Exception {
        Character character = TestUtils.buildCharacter();
        BDDMockito.given(characterRepository.findById("1")).willReturn(Mono.just(character));
        BDDMockito.given(characterRepository.delete(character)).willReturn(Mono.empty());
        webTestClient
                .delete().uri("/characters/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}


