package io.festerson.rpgvault.repository;

import io.festerson.rpgvault.domain.Campaign;
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
import static org.assertj.core.api.Assertions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampaignRepositoryTest {

    @Autowired
    CampaignRepository repository;

    @Autowired
    ReactiveMongoOperations operations;

    @Before
    public void setUp() throws Exception{
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        operations.collectionExists(Campaign.class)
                .flatMap(exists -> exists ? operations.dropCollection(Campaign.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Campaign.class, new CollectionOptions(1024L * 1024L, 100L, true)))
                .then()
                .block();
        repository
                .saveAll(Flux.just(
                        new Campaign("Test One", date.parse("2001-1-10"), date.parse("2002-1-11"), Arrays.asList("p10", "p11", "ptest"), Arrays.asList("c12", "c13"), Arrays.asList("n14", "n15"),  Arrays.asList("m16", "m17"), "18", "Description 1", "http://example.com/img1"),
                        new Campaign("Test Two", date.parse("2002-2-20"), date.parse("2003-2-21"), Arrays.asList("p20", "p21"), Arrays.asList("c22", "c23"), Arrays.asList("n24", "n25"),  Arrays.asList("m26", "m27"), "28", "Description 2", "http://example.com/img2"),
                        new Campaign("Test Three", date.parse("2003-3-30"), date.parse("2004-3-31"), Arrays.asList("p30", "p31", "ptest"), Arrays.asList("c32", "c33"), Arrays.asList("n34", "n35"),  Arrays.asList("m36", "m37"), "38", "Description 3", "http://example.com/img3"))).then().block();
    }

    @Test
    public void getCampaignByName() {
        Campaign campaign = repository.findByName("Test One").block();
        assertThat(campaign).isNotNull();
    }

    @Test
    public void getCampaignsByPlayerIdTest() {

        List<Campaign> campaigns = repository.getCampaignsByPlayerId("ptest").collectList().block();
        assertThat(campaigns).hasSize(2);
    }

    @Test
    public void findAllCampaignsTest() {

        List<Campaign> ids = repository.findAll().collectList().block();
        assertThat(ids).hasSize(3);
    }

}
