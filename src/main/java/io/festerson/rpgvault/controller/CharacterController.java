package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.validator.CharacterValidator;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@CommonsLog
@RestController
@RequestMapping(value="/v1/characters")
public class CharacterController {

    private final CharacterRepository characterRepository;

    private final Validator validator = new CharacterValidator();

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    // Is there way to duplicate how the handler does this?
    // Controller do not have access to ServerRequest.
    @RequestMapping(value="", method = RequestMethod.GET)
    public Mono<ResponseEntity<List<Character>>> getCharacters(@RequestParam(value="player", required=false) String player) {
        if (player == null || player.isEmpty()) {
            return characterRepository.findAll()
                    .collectList()
                    .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
        }
        return characterRepository.getCharactersByPlayerId(player)
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @RequestMapping(value="/{characterId}", method = RequestMethod.GET)
    public Mono<ResponseEntity<Character>> getCharacter(@PathVariable String characterId) {
        return characterRepository.findById(characterId)
            .map(character -> ResponseEntity.ok().body(character))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST)
    public Mono<ResponseEntity<Character>> saveCharacter(@Valid @RequestBody Character character) {
        return characterRepository.save(character)
            .map(saved -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(saved))
            .defaultIfEmpty(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
    }

    @RequestMapping(value="/{characterId}", method = RequestMethod.PUT)
    public Mono<ResponseEntity<Character>> updateCharacter(@Valid @RequestBody Character character, @PathVariable String characterId){
        return characterRepository.findById(characterId)
            .flatMap(found -> {
                found.setName(character.getName());
                found.setCrace(character.getCrace());
                found.setCclass(character.getCclass());
                found.setLevel(character.getLevel());
                found.setStrength(character.getStrength());
                found.setDexterity(character.getDexterity());
                found.setConstitution(character.getConstitution());
                found.setIntelligence(character.getIntelligence());
                found.setWisdom(character.getWisdom());
                found.setCharisma(character.getCharisma());
                found.setAc(character.getAc());
                found.setHp(character.getHp());
                found.setCtype(character.getCtype());
                found.setImageUrl(character.getImageUrl());
                found.setPlayerId(character.getPlayerId());
                return characterRepository.save(found);
            } )
            .map(updatedCharacter -> ResponseEntity.ok(updatedCharacter))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(value="/{characterId}", method = RequestMethod.DELETE)
    public Mono<ResponseEntity<Void>> deleteCharacter(@PathVariable String characterId){
        return characterRepository.findById(characterId)
            .flatMap(toDelete ->
                    characterRepository.delete(toDelete)
                            .then(Mono.just(ResponseEntity.noContent().<Void>build()))
            )
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
