package io.festerson.npcvault;

import io.festerson.npcvault.handler.CampaignHandler;
import io.festerson.npcvault.handler.CharacterHandler;
import io.festerson.npcvault.handler.PlayerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
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
                .route(RequestPredicates.POST("/campaigns").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), campaignHandler::saveCampaign)
                .andRoute(GET("/campaigns"), campaignHandler::getCampaigns)
                .andRoute(GET("/campaigns/{id}"), campaignHandler::getCampaign)
                .andRoute(PUT("/campaigns/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), campaignHandler::updateCampaign)
                .andRoute(DELETE("/campaigns/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), campaignHandler::deleteCampaign)

                .andRoute(POST("/players").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), playerHandler::savePlayer)
                .andRoute(GET("/players"), playerHandler::getPlayers)
                .andRoute(GET("/players/{id}"), playerHandler::getPlayer)
                .andRoute(PUT("/players/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), playerHandler::updatePlayer)
                .andRoute(DELETE("/players/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), playerHandler::deletePlayer)

                .andRoute(POST("/characters").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), characterHandler::saveCharacter)
                .andRoute(GET("/characters"), characterHandler::getCharacters)
                .andRoute(GET("/characters/{id}"), characterHandler::getCharacter)
                .andRoute(PUT("/characters/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), characterHandler::updateCharacter)
                .andRoute(DELETE("/characters/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), characterHandler::deleteCharacter);

    }
}
