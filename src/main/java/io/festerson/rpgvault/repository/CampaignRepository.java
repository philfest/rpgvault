package io.festerson.rpgvault.repository;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.domain.Character;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CampaignRepository extends ReactiveMongoRepository<Campaign, String> {

    Flux<String> findPlayerIdsById(String id);

    @Query("{ 'playerIds': ?0 }")
    Flux<Campaign> getCampaignsByPlayerId(String playerId);

    Mono<Campaign> findByName(String name);
}
