package io.festerson.rpgvault.repository;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CampaignRepositoryTest {

    @Autowired
    CampaignRepository repository;

    @Autowired
    ReactiveMongoOperations operations;

    @BeforeEach
    public void setUp() throws Exception{
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        operations.collectionExists(Campaign.class)
                .flatMap(exists -> exists ? operations.dropCollection(Campaign.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Campaign.class, new CollectionOptions(1024L * 1024L, 100L, true)))
                .then()
                .block();
        repository
            .saveAll(TestUtils.buildCampaignRepositoryTestCollection()).then().block();
    }

    @Test
    public void getCampaignByName() {
        Campaign campaign = repository.findByName(TestUtils.CAMPAIGN_NAME).block();
        assertThat(campaign).isNotNull();
    }

    @Test
    public void getCampaignsByPlayerId() {

        List<Campaign> campaigns = repository.getCampaignsByPlayerId(TestUtils.PLAYER_ID).collectList().block();
        assertThat(campaigns).hasSize(2);
    }

    @Test
    public void getAllCampaigns() {

        List<Campaign> ids = repository.findAll().collectList().block();
        assertThat(ids).hasSize(5);
    }

}
