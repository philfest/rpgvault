package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.repository.PlayerRepository;
import io.festerson.rpgvault.validator.PlayerValidator;
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
@RequestMapping("/v1/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    private final Validator validator = new PlayerValidator();

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public Mono<ResponseEntity<List<Player>>> getPlayers() {
        return playerRepository.findAll()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @GetMapping("/{playerId}")
    public Mono<ResponseEntity<Player>> getPlayer(@PathVariable String playerId) {
        return playerRepository.findById(playerId)
                .map(player -> ResponseEntity.ok().body(player))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Player>> savePlayer(@RequestBody Player player) {
        validate(player);
        return playerRepository.save(player)
                .map(saved -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(saved))
                .defaultIfEmpty(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @PutMapping("/{playerId}")
    public Mono<ResponseEntity<Player>> updatePlayer(@PathVariable String playerId, @RequestBody Player player){
        validate(player);
        return playerRepository.findById(playerId)
                .flatMap(found -> {
                    found.setName(player.getName());
                    found.setEmail(player.getEmail());
                    found.setImageUrl(player.getImageUrl());
                    return playerRepository.save(found);
                } )
                .map(updatedUser -> ResponseEntity.ok(updatedUser))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{playerId}")
    public Mono<ResponseEntity<Void>> deletePlayer(@PathVariable String playerId){
        return playerRepository.findById(playerId)
                .flatMap(existingUser ->
                        playerRepository.delete(existingUser)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private void validate(Player player){
        Errors errors = new BeanPropertyBindingResult(player, "player");
        validator.validate(player, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

}