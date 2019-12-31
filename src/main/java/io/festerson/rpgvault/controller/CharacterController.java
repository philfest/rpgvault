package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.validator.CharacterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/v1/characters")
public class CharacterController {

    private final CharacterRepository characterRepository;

    private final Validator validator = new CharacterValidator();

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping
    public Mono<ResponseEntity<List<Character>>> getCharacters() {
        return characterRepository.findAll()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @GetMapping("/{characterId}")
    public Mono<ResponseEntity<Character>> getCharacter(@PathVariable String characterId) {
        return characterRepository.findById(characterId)
                .map(character -> ResponseEntity.ok().body(character))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Character>> saveCharacter(@RequestBody Character character) {
        validate(character);
        return characterRepository.save(character)
                .map(saved -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(saved))
                .defaultIfEmpty(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @PutMapping("{characterId}")
    public Mono<ResponseEntity<Character>> updateCharacter(@PathVariable String characterId, @RequestBody Character character){
        validate(character);
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

    @DeleteMapping("/{characterId}")
    public Mono<ResponseEntity<Void>> deleteCharacter(@PathVariable String characterId){
        return characterRepository.findById(characterId)
                .flatMap(existingUser ->
                        characterRepository.delete(existingUser)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private void validate(Character character){
        Errors errors = new BeanPropertyBindingResult(character, "character");
        validator.validate(character, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
