package io.festerson.rpgvault.handler;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterHandlerTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    CharacterRepository characterRepository;

    @Test
    public void testGetCharactersHappyPath() throws Exception {
        Flux<Character> c = TestUtils.generateCharacterFlux();
        BDDMockito.given(characterRepository.findAll()).willReturn(c);
        //BDDMockito.given(repository.getCampaignsByPlayerId(String.class)).willReturn(c);
        webTestClient
                .get().uri("/v2/characters")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Character.class);
    }

    @Test
    public void testGetCharacterByIdHappyPath() throws Exception {
        Mono<Character> character = TestUtils.generateCharacterFlux().next();
        BDDMockito.given(characterRepository.findById("1")).willReturn(character);
        webTestClient
                .get().uri("/v2/characters/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Character.class);
    }

    @Test
    public void testGetCharacterByPlayerIdHappyPath() throws Exception {
        Flux<Character> characters = TestUtils.generateCharacterFlux();
        BDDMockito.given(characterRepository.getCharactersByPlayerId("1")).willReturn(characters);
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/v2/characters")
                                .queryParam("player", "1")
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Character.class);
    }
}
