package io.festerson.rpgvault;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Test
    public void contextLoads() {
        assertTrue(true);
    }

}



