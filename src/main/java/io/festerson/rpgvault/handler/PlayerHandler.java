package io.festerson.rpgvault.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PlayerHandler {

    Mono<ServerResponse> getPlayer(ServerRequest request);

    Mono<ServerResponse> getPlayers(ServerRequest request);

    Mono<ServerResponse> savePlayer (ServerRequest request);

    Mono<ServerResponse> updatePlayer (ServerRequest request);

    Mono<ServerResponse> deletePlayer (ServerRequest request);
}
