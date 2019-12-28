package io.festerson.rpgvault.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface CharacterHandler {

    Mono<ServerResponse> getCharacter(ServerRequest request);

    Mono<ServerResponse> getCharacters(ServerRequest request);

    Mono<ServerResponse> saveCharacter(ServerRequest request);

    Mono<ServerResponse> updateCharacter(ServerRequest request);

    Mono<ServerResponse> deleteCharacter(ServerRequest request);
}
