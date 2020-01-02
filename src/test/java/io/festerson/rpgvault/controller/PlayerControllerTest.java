package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.repository.PlayerRepository;
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
public class PlayerControllerTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    PlayerRepository playerRepository;

    @Test
    public void testGetPlayersHappyPath() throws Exception {
        Flux<Player> c = TestUtils.generatePlayerFlux();
        BDDMockito.given(playerRepository.findAll()).willReturn(c);
        webTestClient
                .get().uri("/v1/players")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class);
    }

    @Test
    public void testGetPlayerByIdHappyPath() throws Exception {
        Mono<Player> player = TestUtils.generatePlayerFlux().next();
        BDDMockito.given(playerRepository.findById("1")).willReturn(player);
        webTestClient
                .get().uri("/v1/players/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class);
    }

}
