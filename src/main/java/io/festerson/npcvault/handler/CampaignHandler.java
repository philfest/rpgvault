package io.festerson.npcvault.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface CampaignHandler {

    Mono<ServerResponse> getCampaign(ServerRequest request);

    Mono<ServerResponse> getCampaigns(ServerRequest request);

    Mono<ServerResponse> saveCampaign(ServerRequest request);

    Mono<ServerResponse> updateCampaign(ServerRequest request);

    Mono<ServerResponse> deleteCampaign(ServerRequest request);
}
