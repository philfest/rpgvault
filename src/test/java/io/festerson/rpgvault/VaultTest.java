package io.festerson.rpgvault;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VaultTest {

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    ReactiveMongoOperations operations;

    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void setup() {
        dropCreateCollections();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        /**campaignRepository
                .saveAll(Flux.just(
                        new Campaign("Test One", date.parse("2001-1-10"), date.parse("2002-1-11"), Arrays.asList("p10", "p11", "ptest"), Arrays.asList("c12", "c13"), Arrays.asList("n14", "n15"), Arrays.asList("m16", "m17"), "18", "Description 1", "http://example.com/img1"),
                        new Campaign("Test Two", date.parse("2002-2-20"), date.parse("2003-2-21"), Arrays.asList("p20", "p21"), Arrays.asList("c22", "c23"), Arrays.asList("n24", "n25"), Arrays.asList("m26", "m27"), "28", "Description 2", "http://example.com/img2"),
                        new Campaign("Test Three", date.parse("2003-3-30"), date.parse("2004-3-31"), Arrays.asList("p30", "p31", "ptest"), Arrays.asList("c32", "c33"), Arrays.asList("n34", "n35"), Arrays.asList("m36", "m37"), "38", "Description 3", "http://example.com/img3"))).then().block();
        */
         characterRepository
                .saveAll(Flux.just(
                        TestUtils.generateCharacter(null),
                        TestUtils.generateCharacter(null),
                        TestUtils.generateCharacter(null))).then().block();

    }

    @After
    public void teardown(){
        //dropCreateCollections();
    }

    private void dropCreateCollections(){
        operations.collectionExists(Campaign.class)
                .flatMap(exists -> exists ? operations.dropCollection(Campaign.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Campaign.class, new CollectionOptions(1024L * 1024L, 100L, true)))
                .then()
                .block();
        operations.collectionExists(Character.class)
                .flatMap(exists -> exists ? operations.dropCollection(Character.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Character.class, new CollectionOptions(1024L * 1024L, 100L, true)))
                .then()
                .block();
    }

    @Test
    public void testPostCharacter(){
        webTestClient
                .post().uri("/characters")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Character.class);
    }

    @Test
    public void testPostPlayer(){
        webTestClient
                .post().uri("/players")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Character.class);
    }

    @Test
    public void testPostCampaign(){
        webTestClient
                .post().uri("/campaigns")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Character.class);
    }

}



