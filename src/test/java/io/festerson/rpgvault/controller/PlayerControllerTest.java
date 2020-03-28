package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.repository.PlayerRepository;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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
public class PlayerControllerTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    PlayerRepository playerRepository;

    @Test
    public void testGetPlayersHappyPath() throws Exception {
        Flux<Player> players = TestUtils.buildPlayerRepositoryTestCollection();
        BDDMockito.given(playerRepository.findAll()).willReturn(players);
        webTestClient
                .get().uri("/players")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Player.class);
    }

    @Test
    public void testGetPlayerByIdHappyPath() throws Exception {
        Mono<Player> player = Mono.just(TestUtils.buildPlayer());
        BDDMockito.given(playerRepository.findById("1")).willReturn(player);
        webTestClient
                .get().uri("/players/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class);
    }

   @Test
    public void testCreatePlayerHappyPath() throws Exception {
       Player player = TestUtils.buildPlayer();
       BDDMockito.given(playerRepository.save(any())).willReturn(Mono.just(player));
       webTestClient
           .post().uri("/players")
           .contentType(MediaType.APPLICATION_JSON)
           .body(Mono.just(player), Player.class)
           .accept(MediaType.APPLICATION_JSON)
           .exchange()
           .expectStatus().isCreated()
           .expectBody(Player.class);
    }


    @Test
    public void testUpdatePlayerHappyPath() throws Exception {
        Player player = TestUtils.buildPlayer();
        BDDMockito.given(playerRepository.save(player)).willReturn(Mono.just(player));
        BDDMockito.given(playerRepository.findById("1")).willReturn(Mono.just(player));
        webTestClient
                .put().uri("/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(player), Player.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Player.class);
    }

    @Test
    public void testDeletePlayerHappyPath() throws Exception {
        Player player = TestUtils.buildPlayer();
        BDDMockito.given(playerRepository.findById("1")).willReturn(Mono.just(player));
        BDDMockito.given(playerRepository.delete(player)).willReturn(Mono.empty());
        webTestClient
                .delete().uri("/players/1")
                .exchange()
                .expectStatus().isNoContent();
    }

}
