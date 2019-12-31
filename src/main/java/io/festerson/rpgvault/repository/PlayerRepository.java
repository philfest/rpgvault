package io.festerson.rpgvault.repository;

import io.festerson.rpgvault.domain.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PlayerRepository extends ReactiveMongoRepository<Player, String> {

}
