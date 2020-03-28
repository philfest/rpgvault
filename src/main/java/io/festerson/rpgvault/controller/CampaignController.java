package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.exception.NotFoundException;
import io.festerson.rpgvault.repository.CampaignRepository;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;

@CommonsLog
@RestController
@AllArgsConstructor
public class CampaignController {

    private CampaignRepository campaignRepository;

    @GetMapping("/campaigns")
    public Mono<ResponseEntity<List<Campaign>>>  getCampaigns(@RequestParam(value="player", required=false) String playerId) {
        if (playerId == null || playerId.isEmpty()) {
            return campaignRepository.findAll()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
        }
        return campaignRepository.getCampaignsByPlayerId(playerId)
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @GetMapping("/campaigns/{campaignId}")
    public Mono<ResponseEntity<Campaign>> getCampaign(@PathVariable String campaignId) {
        return campaignRepository.findById(campaignId)
            .map(character -> ResponseEntity.ok().body(character))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(value="/campaigns", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Campaign>> saveCampaign(@Valid @RequestBody Campaign campaign) {
        return campaignRepository.save(campaign)
            .map(saved -> ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(saved))
            .defaultIfEmpty(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build());
    }

    @PutMapping("/campaigns/{campaignId}")
    public Mono<ResponseEntity<Campaign>> updateCampaign(@Valid @RequestBody Campaign campaign, @PathVariable String campaignId){
        return campaignRepository.findById(campaignId)
            .flatMap(found -> {
                found.setName(campaign.getName());
                found.setStartDate(campaign.getStartDate());
                found.setEndDate(campaign.getEndDate());
                found.setPlayerIds(campaign.getPlayerIds());
                found.setCharacterIds(campaign.getCharacterIds());
                found.setNpcIds(campaign.getNpcIds());
                found.setMonsterIds(campaign.getMonsterIds());
                found.setDmId(campaign.getDmId());
                found.setDescription(campaign.getDescription());
                found.setImageUrl(campaign.getImageUrl());
                return  campaignRepository.save(found);
            })
            .map(updatedCharacter -> ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedCharacter))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/campaigns/{campaignId}")
    public  Mono<ResponseEntity<Void>> deleteCampaign(@PathVariable String campaignId){
        return campaignRepository.findById(campaignId)
            .flatMap(toDelete ->
                campaignRepository.delete(toDelete)
                    .then(Mono.just(ResponseEntity.noContent().<Void>build()))
            )
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
