package io.festerson.rpgvault.controller;

import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.validator.CampaignValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/v1/campaigns")
public class CampaignController {

    private final CampaignRepository campaignRepository;

    private final Validator validator = new CampaignValidator();

    @Autowired
    public CampaignController(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @GetMapping
    public Mono<ResponseEntity<List<Campaign>>> getCampaigns() {
        return campaignRepository.findAll()
                .collectList()
                .map(list -> ResponseEntity.ok().contentType(APPLICATION_JSON).body(list));
    }

    @GetMapping(path = "/{campaignId}")
    public Mono<ResponseEntity<Campaign>> getCampaign(@PathVariable String campaignId) {
        return campaignRepository.findById(campaignId)
                .map(campaign -> ResponseEntity.ok().body(campaign))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Campaign>> saveCampaign(@RequestBody Campaign campaign) {
        validate(campaign);
        return campaignRepository.save(campaign)
                .map(saved -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(saved))
                .defaultIfEmpty(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @PutMapping("/{campaignId}")
    public Mono<ResponseEntity<Campaign>> updateCampaign(@PathVariable String campaignId, @RequestBody Campaign campaign){
        validate(campaign);
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

    @DeleteMapping("/{campaignId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCampaign(@PathVariable String campaignId){
        return campaignRepository.findById(campaignId)
                .flatMap(deletedCampaign ->
                        campaignRepository.delete(deletedCampaign)
                                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private void validate(Campaign campaign){
        Errors errors = new BeanPropertyBindingResult(campaign, "campaign");
        validator.validate(campaign, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
