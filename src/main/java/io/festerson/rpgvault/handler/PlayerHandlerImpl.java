package io.festerson.rpgvault.handler;

import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.repository.PlayerRepository;
import io.festerson.rpgvault.validator.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;



@Component
public class PlayerHandlerImpl extends RpgVaultAbstractHandler implements PlayerHandler {

    private final PlayerRepository playerRepository;

    private final Validator validator = new PlayerValidator();

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();


    @Autowired
    public PlayerHandlerImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<ServerResponse> getPlayers(ServerRequest request) {
        return this.playerRepository.findAll()
                .collectList()
                .flatMap(list -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(list)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> getPlayer(ServerRequest request) {
        String playerId = request.pathVariable("id");
        return this.playerRepository.findById(playerId)
                .flatMap(player -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(player)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> savePlayer(ServerRequest request) {
        return request.bodyToMono(Player.class)
                .doOnNext(this::validate)
                .flatMap(playerRepository::save)
                .flatMap(saved -> ServerResponse.status(201).contentType(APPLICATION_JSON).body(fromObject(saved)))
                .switchIfEmpty(ServerResponse.status(500).build());
    }

    public Mono<ServerResponse> updatePlayer(ServerRequest request) {
        String pathId = request.pathVariable("id");
        return request.bodyToMono(Player.class)
                .doOnNext(this::validate)
                .filter(player -> player.getId().equals(pathId))
                .filterWhen(valid ->
                        this.playerRepository.existsById(valid.getId()).map(exists -> exists.booleanValue())
                )
                .flatMap(playerRepository::save)
                .flatMap(updated -> ServerResponse.status(200).contentType(APPLICATION_JSON).body(fromObject(updated)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public  Mono<ServerResponse> deletePlayer(ServerRequest request) {
        String playerId = request.pathVariable("id");
        // Cannot return a 404 when using a reactive delete because it returns a Mono<Void>
        // switchIfEmpty() is too eager and will trigger whether delete happens or not.
        // Annotation-based reactive model does allow more control
        return this.playerRepository.deleteById(playerId).then(ServerResponse.noContent().build());
    }

    // Functional endpoints do not have @RequestBody and java.validation annotations exposed
    // to them. They are not traditional @RestController endpoints and take ServerRequest as a parameter.
    private void validate(Player player){
        Errors errors = new BeanPropertyBindingResult(player, "player");
        validator.validate(player, errors);
        manageValidationErrors(errors);
    }
}
