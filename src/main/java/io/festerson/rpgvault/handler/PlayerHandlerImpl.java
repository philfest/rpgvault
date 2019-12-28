package io.festerson.rpgvault.handler;

import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class PlayerHandlerImpl implements PlayerHandler {

    private final PlayerRepository playerRepository;

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public PlayerHandlerImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<ServerResponse> getPlayer(ServerRequest request) {
        String playerId = request.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Player> player = this.playerRepository.findById(playerId);
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(player, Player.class).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getPlayers(ServerRequest request) {
        Flux<Player> players = this.playerRepository.findAll();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(players, Player.class);
    }

    public Mono<ServerResponse> savePlayer(ServerRequest request) {
        Mono<Player> player = request.bodyToMono(Player.class);
        Mono<Player> playerSaved = player.flatMap(playerRepository::save);
        return ServerResponse.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(playerSaved, Player.class);
    }

    public Mono<ServerResponse> updatePlayer(ServerRequest request) {
        String pathId = request.pathVariable("id");
        Mono<Player> playerToUpdate = request.bodyToMono(Player.class);
        Mono<Player> updatedPlayer = playerToUpdate
                .filter(player -> player.getId().equals(pathId))
                .filterWhen(valid ->
                        this.playerRepository.existsById(valid.getId()).map(exists -> exists.booleanValue())
                )
                .flatMap( c ->  this.playerRepository.save(c));

        return updatedPlayer
                .flatMap(player -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(player)))
                .switchIfEmpty(NOT_FOUND);
    }

    public Mono<ServerResponse> deletePlayer(ServerRequest request) {
        String playerId = request.pathVariable("id");
        Mono<Void> playerMono = this.playerRepository.deleteById(playerId);
        return playerMono
                .flatMap(player -> ServerResponse.noContent().build())
                .switchIfEmpty(NOT_FOUND);
    }
}
