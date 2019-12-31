package io.festerson.rpgvault;

import io.festerson.rpgvault.handler.CampaignHandler;
import io.festerson.rpgvault.handler.CharacterHandler;
import io.festerson.rpgvault.handler.PlayerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class Router {

    @Autowired
    CampaignHandler campaignHandler;

    @Autowired
    PlayerHandler playerHandler;

    @Autowired
    CharacterHandler characterHandler;

    @Bean
    public RouterFunction<ServerResponse> route() {

        return RouterFunctions
            .route(RequestPredicates.POST("/v2/campaigns").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), campaignHandler::saveCampaign)
            .andRoute(GET("/v2/campaigns"), campaignHandler::getCampaigns)
            .andRoute(GET("/v2/campaigns/{id}"), campaignHandler::getCampaign)
            .andRoute(PUT("/v2/campaigns/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), campaignHandler::updateCampaign)
            .andRoute(DELETE("/v2/campaigns/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), campaignHandler::deleteCampaign)

            .andRoute(POST("/v2/players").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), playerHandler::savePlayer)
            .andRoute(GET("/v2/players"), playerHandler::getPlayers)
            .andRoute(GET("/v2/players/{id}"), playerHandler::getPlayer)
            .andRoute(PUT("/v2/players/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), playerHandler::updatePlayer)
            .andRoute(DELETE("/v2/players/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), playerHandler::deletePlayer)

            .andRoute(POST("/v2/characters").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), characterHandler::saveCharacter)
            .andRoute(GET("/v2/characters"), characterHandler::getCharacters)
            .andRoute(GET("/v2/characters/{id}"), characterHandler::getCharacter)
            .andRoute(PUT("/v2/characters/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), characterHandler::updateCharacter)
            .andRoute(DELETE("/v2/characters/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), characterHandler::deleteCharacter);
    }
}
