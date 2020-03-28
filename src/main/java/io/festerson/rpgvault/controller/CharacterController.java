package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CharacterRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@CommonsLog
@RestController
public class CharacterController {

    private final CharacterRepository characterRepository;

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    // Is there way to duplicate how the handler does this?
    // Controller do not have access to ServerRequest.
    @GetMapping("/characters")
    public Mono<ResponseEntity<List<Character>>> getCharacters(@RequestParam(value="player", required=false) String characterId) {
        if (characterId == null || characterId.isEmpty()) {
            return characterRepository.findAll()
                    .collectList()
                    .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
        }
        return characterRepository.getCharactersByPlayerId(characterId)
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @GetMapping("/characters/{characterId}")
    public Mono<ResponseEntity<Character>> getCharacter(@PathVariable String characterId) {
        return characterRepository.findById(characterId)
            .map(character -> ResponseEntity.ok().body(character))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/characters")
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

    @PutMapping("/characters/{characterId}")
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
            .map(updatedCharacter -> ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(updatedCharacter))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/characters/{characterId}")
    public Mono<ResponseEntity<Void>> deleteCharacter(@PathVariable String characterId){
        return characterRepository.findById(characterId)
            .flatMap(toDelete ->
                    characterRepository.delete(toDelete)
                            .then(Mono.just(ResponseEntity.noContent().<Void>build()))
            )
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
