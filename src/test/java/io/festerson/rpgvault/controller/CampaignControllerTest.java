package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.repository.CampaignRepository;
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
public class CampaignControllerTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    CampaignRepository campaignRepository;

    @Test
    public void testGetCampaignsHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        BDDMockito.given(campaignRepository.findAll()).willReturn(campaigns);
        webTestClient
                .get().uri("/campaigns")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Campaign.class);
    }

    @Test
    public void testGetCampaignByIdHappyPath() throws Exception {
        Mono<Campaign> campaign = Mono.just(TestUtils.buildCampaign());
        BDDMockito.given(campaignRepository.findById("1")).willReturn(campaign);
        webTestClient
                .get().uri("/campaigns/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Campaign.class);
    }

    @Test
    public void testGetCampaignsByPlayerIdHappyPath() throws Exception {
        Flux<Campaign> campaigns = TestUtils.buildCampaignRepositoryTestCollection();
        BDDMockito.given(campaignRepository.getCampaignsByPlayerId("1")).willReturn(campaigns);
        webTestClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder
                            .path("/campaigns")
                            .queryParam("player", "1")
                            .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Campaign.class);
    }

    @Test
    public void testCreateCampaignHappyPath() throws Exception {
        Campaign campaign = TestUtils.buildCampaign();
        BDDMockito.given(campaignRepository.save(any())).willReturn(Mono.just(campaign));
        webTestClient
                .post()
                .uri("/campaigns")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(TestUtils.buildCampaign()))// (Mono.just(TestUtils.buildCampaign()), Campaign.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Campaign.class);
    }

    @Test
    public void testUpdateCampaignHappyPath() throws Exception {
        Campaign campaign = TestUtils.buildCampaign();
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
                .expectBody(Campaign.class);
    }

    @Test
    public void testDeleteCampaignHappyPath() throws Exception {
        Campaign campaign = TestUtils.buildCampaign();
        BDDMockito.given(campaignRepository.findById("1")).willReturn(Mono.just(campaign));
        BDDMockito.given(campaignRepository.delete(campaign)).willReturn(Mono.empty());
        webTestClient
                .delete().uri("/campaigns/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
