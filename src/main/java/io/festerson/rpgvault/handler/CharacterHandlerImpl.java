package io.festerson.rpgvault.handler;

import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CharacterRepository;
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
public class CharacterHandlerImpl implements CharacterHandler{

    private final CharacterRepository characterRepository;

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CharacterHandlerImpl(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public Mono<ServerResponse> getCharacter(ServerRequest request) {
        String characterId = request.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Character> character = this.characterRepository.findById(characterId);
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(character, Character.class).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getCharacters(ServerRequest request) {
      Flux<Character> characters = request.queryParam("player")
                .map(player -> characterRepository.getCharactersByPlayerId(player))
                .orElseGet(() -> characterRepository.findAll());
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(characters, Character.class);

    }

    public Mono<ServerResponse> saveCharacter(ServerRequest request) {
        Mono<Character> character = request.bodyToMono(Character.class);
        Mono<Character> characterSaved = character.flatMap(characterRepository::save);
        return ServerResponse.status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(characterSaved, Character.class);
    }

    public Mono<ServerResponse> updateCharacter(ServerRequest request) {
        String pathId = request.pathVariable("id");
        Mono<Character> characterToUpdate = request.bodyToMono(Character.class);
        Mono<Character> updatedCharacter = characterToUpdate
                .filter(character -> character.getId().equals(pathId))
                .filterWhen(valid ->
                        this.characterRepository.existsById(valid.getId()).map(exists -> exists.booleanValue())
                )
                .flatMap( c ->  this.characterRepository.save(c));

        return updatedCharacter
                .flatMap(character -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(character)))
                .switchIfEmpty(NOT_FOUND);
    }

    public Mono<ServerResponse> deleteCharacter(ServerRequest request) {
        String characterId = request.pathVariable("id");
        Mono<Void> characterMono = this.characterRepository.deleteById(characterId);
        return characterMono
                .flatMap(character -> ServerResponse.noContent().build())
                .switchIfEmpty(NOT_FOUND);
    }
}
