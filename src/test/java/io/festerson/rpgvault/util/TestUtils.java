package io.festerson.rpgvault.util;

import io.festerson.rpgvault.domain.*;
import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestUtils {

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    PlayerRepository playerRepository;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final List<String> FIRST_NAMES = Arrays.asList("Ragnor", "Drac", "Corellion", "Thunk", "Garl", "Brevnor", "Vera");
    public static final List<String> LAST_NAMES = Arrays.asList("Stonebane", "Mannix", "Brightstar", "Goreman", "Lightfingers", "Pox", "Krinkles");

    public static int generateAbility(){
        Random RND = new Random();
        return RND.nextInt((18 - 8) + 1) + 8;
    }

    public static int generateAc(){
        Random RND = new Random();
        return RND.nextInt((18 - 11) + 1) + 11;
    }

    public static int generateHp(){
        Random RND = new Random();
        return RND.nextInt((14 - 8) + 1) + 8;
    }

    public static Character generateCharacter(String name){

        if (name == null || name.isEmpty()) {
            Collections.shuffle(FIRST_NAMES);
            Collections.shuffle(LAST_NAMES);
            name = FIRST_NAMES.get(0) + LAST_NAMES.get(0);
        }

        return new Character(
                name,
                CharacterRace.pickOne(),
                CharacterClass.pickOne(),
                1,
                TestUtils.generateAbility(),
                TestUtils.generateAbility(),
                TestUtils.generateAbility(),
                TestUtils.generateAbility(),
                TestUtils.generateAbility(),
                TestUtils.generateAbility(),
                TestUtils.generateAc(),
                TestUtils.generateHp(),
                0,
                CharacterType.PC,
                null,
                null);
    }

    public static Player generatePlayer(String name){

        if (name == null || name.isEmpty()) {
            Collections.shuffle(FIRST_NAMES);
            Collections.shuffle(LAST_NAMES);
            name = FIRST_NAMES.get(0) + LAST_NAMES.get(0);
        }

        return new Player(
                name,
                "me@notu.com",
                "http://example.com/img1"
                );
    }

    public void preFillCampaigns() throws Exception{
        campaignRepository.saveAll(Flux.just(
                        new Campaign("Test One", dateFormat.parse("2001-1-10"), dateFormat.parse("2002-1-11"), Arrays.asList("p10", "p11", "ptest"), Arrays.asList("c12", "c13"), Arrays.asList("n14", "n15"),  Arrays.asList("m16", "m17"), "18", "Description 1", "http://example.com/img1"),
                        new Campaign("Test Two", dateFormat.parse("2002-2-20"), dateFormat.parse("2003-2-21"), Arrays.asList("p20", "p21"), Arrays.asList("c22", "c23"), Arrays.asList("n24", "n25"),  Arrays.asList("m26", "m27"), "28", "Description 2", "http://example.com/img2"),
                        new Campaign("Test Three", dateFormat.parse("2003-3-30"), dateFormat.parse("2004-3-31"), Arrays.asList("p30", "p31", "ptest"), Arrays.asList("c32", "c33"), Arrays.asList("n34", "n35"),  Arrays.asList("m36", "m37"), "38", "Description 3", "http://example.com/img3"))).then().block();


    }

    public void preFillCharacters(){
        characterRepository.saveAll(Flux.just(
                        TestUtils.generateCharacter(null),
                        TestUtils.generateCharacter(null),
                        TestUtils.generateCharacter(null))).then().block();
    }

    public void preFillPlayers(){
        playerRepository.saveAll(Flux.just(
                TestUtils.generatePlayer(null),
                TestUtils.generatePlayer(null),
                TestUtils.generatePlayer(null))).then().block();
    }

    public static void main(String[] args){
        TestUtils utils = new TestUtils();

        utils.preFillPlayers();

    }
}
