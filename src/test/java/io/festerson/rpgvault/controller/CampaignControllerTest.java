package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.Application;
import io.festerson.rpgvault.Router;
import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(Router.class)
public class CampaignControllerTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    CampaignRepository campaignRepository;

    @Test
    public void testGetCampaignsHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.generateCampaignFlux();
        BDDMockito.given(campaignRepository.findAll()).willReturn(campaigns);
        webTestClient
                .get().uri("/v1/campaigns")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Campaign.class);
    }

    @Test
    public void testGetCampaignByIdHappyPath() throws Exception {
        Mono<Campaign> campaign = TestUtils.generateCampaignFlux().next();
        BDDMockito.given(campaignRepository.findById("1")).willReturn(campaign);
        webTestClient
                .get().uri("/v1/campaigns/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Campaign.class);
    }

    @Test
    public void testGetCampaignsByPlayerIdHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.generateCampaignFlux();
        BDDMockito.given(campaignRepository.getCampaignsByPlayerId("1")).willReturn(campaigns);
        webTestClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder
                            .path("/v1/characters")
                            .queryParam("player", "1")
                            .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Character.class);
    }
}
