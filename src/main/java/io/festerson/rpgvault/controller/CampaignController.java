package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.validator.CampaignValidator;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@CommonsLog
@RestController
@RequestMapping(value="/v1/campaigns")
public class CampaignController {

    private final CampaignRepository campaignRepository;

    private final Validator validator = new CampaignValidator();

    @Autowired
    public CampaignController(CampaignRepository campaignRepository) { this.campaignRepository = campaignRepository; }

    @RequestMapping(value="", method = RequestMethod.GET)
    public Mono<ResponseEntity<List<Campaign>>> getCampaigns() {
        return campaignRepository.findAll()
            .collectList()
            .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @RequestMapping(path = "/{campaignId}", method = RequestMethod.GET)
    public Mono<ResponseEntity<Campaign>> getCampaign(@PathVariable String campaignId) {
        return campaignRepository.findById(campaignId)
            .map(campaign -> ResponseEntity.ok().body(campaign))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST)
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

    @RequestMapping(value="/{campaignId}", method = RequestMethod.PUT)
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
                return campaignRepository.save(found);
            } )
            .map(updatedCampaign -> ResponseEntity.ok(updatedCampaign))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @RequestMapping(value="/{campaignId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCampaign(@PathVariable String campaignId){
        return campaignRepository.findById(campaignId)
            .flatMap(deletedCampaign ->
                    campaignRepository.delete(deletedCampaign)
                            .then(Mono.just(ResponseEntity.noContent().<Void>build()))
            )
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
