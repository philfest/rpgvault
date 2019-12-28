package io.festerson.rpgvault.handler;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.repository.CampaignRepository;
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
public class CampaignHandlerImpl implements CampaignHandler {

    private final CampaignRepository campaignRepository;

    private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

    @Autowired
    public CampaignHandlerImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Mono<ServerResponse> getCampaigns(ServerRequest request) {
        Flux<Campaign> campaigns = request.queryParam("player")
                .map(player -> campaignRepository.getCampaignsByPlayerId(player))
                .orElseGet(() -> campaignRepository.findAll());
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(campaigns, Campaign.class);
    }

    public Mono<ServerResponse> getCampaign(ServerRequest request) {
        String campaignId = request.pathVariable("id");
        //Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Campaign> campaignMono = this.campaignRepository.findById(campaignId);
        return campaignMono
                .flatMap(campaign -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(campaign)))
                .switchIfEmpty(NOT_FOUND);
    }

    public Mono<ServerResponse> saveCampaign(ServerRequest request) {
        Mono<Campaign> campaign = request.bodyToMono(Campaign.class);
        Mono<Campaign> campaignSaved = campaign.flatMap(campaignRepository::save);
        return ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(campaignSaved, Campaign.class);
    }

    public Mono<ServerResponse> updateCampaign(ServerRequest request) {
        String pathId = request.pathVariable("id");
        Mono<Campaign> campaignToUpdate = request.bodyToMono(Campaign.class);
        //Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Campaign> updatedCampaign = campaignToUpdate
                .filter(campaign -> campaign.getId().equals(pathId))
                .filterWhen(valid ->
                    this.campaignRepository.existsById(valid.getId()).map(exists -> exists.booleanValue())
                )
                .flatMap( c ->  this.campaignRepository.save(c));

        return updatedCampaign
                .flatMap(campaign -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(campaign)))
                .switchIfEmpty(NOT_FOUND);
    }

    public Mono<ServerResponse> deleteCampaign(ServerRequest request) {
        String campaignId = request.pathVariable("id");
        //Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Void> campaignMono = this.campaignRepository.deleteById(campaignId);
        return campaignMono
                .flatMap(campaign -> ServerResponse.noContent().build())
                .switchIfEmpty(NOT_FOUND);
    }

}
