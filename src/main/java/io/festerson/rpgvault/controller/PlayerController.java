package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.repository.PlayerRepository;
import io.festerson.rpgvault.validator.PlayerValidator;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
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

@CommonsLog
@RestController
@RequestMapping(value="/v1/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    private final Validator validator = new PlayerValidator();

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public Mono<ResponseEntity<List<Player>>> getPlayers() {
        return playerRepository.findAll()
                .collectList()
                .map(allFound -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(allFound));
    }

    @RequestMapping(value="/{playerId}", method = RequestMethod.GET)
    public Mono<ResponseEntity<Player>> getPlayer(@PathVariable String playerId) {
        return playerRepository.findById(playerId)
                .map(found -> ResponseEntity.ok().body(found))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST)
    public Mono<ResponseEntity<Player>> savePlayer(@Valid @RequestBody Player player) {
        return playerRepository.save(player)
                .map(saved -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(saved))
                .defaultIfEmpty(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @RequestMapping(value="/{playerId}", method = RequestMethod.PUT)
    public Mono<ResponseEntity<Player>> updatePlayer(@Valid @RequestBody Player player, @PathVariable String playerId){
        return playerRepository.findById(playerId)
                .flatMap(toUpdate -> {
                    toUpdate.setName(player.getName());
                    toUpdate.setEmail(player.getEmail());
                    toUpdate.setImageUrl(player.getImageUrl());
                    return playerRepository.save(toUpdate);
                } )
                .map(updatedPlayer -> ResponseEntity.ok(updatedPlayer))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(value="/{playerId}", method = RequestMethod.DELETE)
    public Mono<ResponseEntity<Void>> deletePlayer(@PathVariable String playerId){
        return playerRepository.findById(playerId)
                .flatMap(toDelete ->
                        playerRepository.delete(toDelete)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
