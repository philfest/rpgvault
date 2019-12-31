package io.festerson.rpgvault.handler;

import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.validator.CharacterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;


@Component
public class CharacterHandlerImpl implements CharacterHandler{

    private final CharacterRepository characterRepository;

    private final Validator validator = new CharacterValidator();

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CharacterHandlerImpl(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public Mono<ServerResponse> getCharacters(ServerRequest request) {
        return request.queryParam("player")
                .map(player -> characterRepository.getCharactersByPlayerId(player))
                .orElseGet(() -> characterRepository.findAll())
                .collectList()
                .flatMap(list -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(list)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> getCharacter(ServerRequest request) {
        String characterId = request.pathVariable("id");
        return this.characterRepository.findById(characterId)
                .flatMap(character -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(character)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> saveCharacter(ServerRequest request) {
        return request.bodyToMono(Character.class)
                .doOnNext(this::validate)
                .flatMap(characterRepository::save)
                .flatMap(saved -> ServerResponse.status(201).contentType(APPLICATION_JSON).body(fromObject(saved)))
                .switchIfEmpty(ServerResponse.status(500).build());
    }

    public Mono<ServerResponse> updateCharacter(ServerRequest request) {
        String pathId = request.pathVariable("id");
        return request.bodyToMono(Character.class)
                .doOnNext(this::validate)
                .filter(character -> character.getId().equals(pathId))
                .filterWhen(valid ->
                        this.characterRepository.existsById(valid.getId()).map(exists -> exists.booleanValue())
                )
                .flatMap(characterRepository::save)
                .flatMap(updated -> ServerResponse.status(200).contentType(APPLICATION_JSON).body(fromObject(updated)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> deleteCharacter(ServerRequest request) {
        String characterId = request.pathVariable("id");
        // Cannot return a 404 when using a reactive delete because it returns a Mono<Void>
        // switchIfEmpty() is too eager and will trigger whether delete happens or not.
        // Annotation-based reactive model does allow more control
        return this.characterRepository.deleteById(characterId).then(ServerResponse.noContent().build());
    }

    private void validate(Character character){
        Errors errors = new BeanPropertyBindingResult(character, "character");
        validator.validate(character, errors);
        if (errors.hasErrors()) {
            List<FieldError> fieldErrors = errors.getFieldErrors();
            StringBuilder errorMessages = new StringBuilder();
            for(FieldError e : fieldErrors){
                errorMessages.append(e.getDefaultMessage() + " ");
            }
            throw new ServerWebInputException(errorMessages.toString());
        }    }
}
