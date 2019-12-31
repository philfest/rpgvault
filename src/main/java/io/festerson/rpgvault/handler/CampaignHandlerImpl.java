package io.festerson.rpgvault.handler;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.validator.CampaignValidator;
import lombok.extern.apachecommons.CommonsLog;
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

@CommonsLog
@Component
public class CampaignHandlerImpl implements CampaignHandler {

    private final CampaignRepository campaignRepository;

    private final Validator validator = new CampaignValidator();

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CampaignHandlerImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Mono<ServerResponse> getCampaigns(ServerRequest request) {
        return request.queryParam("player")
                .map(player -> campaignRepository.getCampaignsByPlayerId(player))
                .orElseGet(() -> campaignRepository.findAll())
                .collectList()
                .flatMap(list -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(list)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> getCampaign(ServerRequest request) {
        String campaignId = request.pathVariable("id");
        return this.campaignRepository.findById(campaignId)
                .flatMap(campaign -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(campaign)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> saveCampaign(ServerRequest request) {
       return request.bodyToMono(Campaign.class)
               .doOnNext(this::validate)
               .flatMap(campaignRepository::save)
               .flatMap(saved -> ServerResponse.status(201).contentType(APPLICATION_JSON).body(fromObject(saved)))
               .switchIfEmpty(ServerResponse.status(500).build());
    }

    public Mono<ServerResponse> updateCampaign(ServerRequest request) {
        String pathId = request.pathVariable("id");
        return request.bodyToMono(Campaign.class)
                .doOnNext(this::validate)
                .filter(campaign -> campaign.getId().equals(pathId))
                .filterWhen(valid ->
                        this.campaignRepository.existsById(valid.getId()).map(exists -> exists.booleanValue())
                )
                .flatMap(campaignRepository::save)
                .flatMap(updated -> ServerResponse.status(200).contentType(APPLICATION_JSON).body(fromObject(updated)))
                .switchIfEmpty(ServerResponse.status(404).build());
    }

    public Mono<ServerResponse> deleteCampaign(ServerRequest request) {
        String campaignId = request.pathVariable("id");
        // Cannot return a 404 when using a reactive delete because it returns a Mono<Void>
        // switchIfEmpty() is too eager and will trigger whether delete happens or not.
        // Annotation-based reactive model does allow more control
        return campaignRepository.deleteById(campaignId).then(ServerResponse.noContent().build());
    }

    // Functional endpoints do not have @RequestBody and java.validation annotations exposed
    // to them. They are not traditional @RestController endpoints and take ServerRequest as a parameter.
    private void validate(Campaign campaign){
        Errors errors = new BeanPropertyBindingResult(campaign, "campaign");
        validator.validate(campaign, errors);
        if (errors.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            List<FieldError> fieldErrors = errors.getFieldErrors();
            fieldErrors.forEach(e -> errorMessages.append(e.getDefaultMessage() + " "));
            throw new ServerWebInputException(errorMessages.toString());
        }
    }
}
