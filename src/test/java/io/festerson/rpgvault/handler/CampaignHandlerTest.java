package io.festerson.rpgvault.handler;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampaignHandlerTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    CampaignRepository campaignRepository;

    @Test
    public void testGetCampaignsHappyPath() throws Exception {
        Flux<Campaign> c = TestUtils.generateCampaignFlux();
        BDDMockito.given(campaignRepository.findAll()).willReturn(c);
        webTestClient
                .get().uri("/v2/campaigns")
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
                .get().uri("/v2/campaigns/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Campaign.class);
    }
}
