package io.festerson.rpgvault.repository;

import io.festerson.rpgvault.domain.Character;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CharacterRepository extends ReactiveMongoRepository<Character, String> {

        @Query("{ 'name': ?0 }")
        Mono<Character> findByName(String name);

        @Query("{ 'playerId': ?0 }")
        Flux<Character> getCharactersByPlayerId(String playerId);
}
