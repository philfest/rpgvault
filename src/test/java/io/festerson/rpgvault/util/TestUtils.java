package io.festerson.rpgvault.util;

import io.festerson.rpgvault.domain.*;
import io.festerson.rpgvault.domain.Character;
import io.festerson.rpgvault.repository.CampaignRepository;
import io.festerson.rpgvault.repository.CharacterRepository;
import io.festerson.rpgvault.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.text.SimpleDateFormat;
import java.util.*;

public class TestUtils {

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    PlayerRepository playerRepository;

    public static final String PLAYER_ID = "TEST_ID";
    public static final String PLAYER_NAME = "TEST_PLAYER";
    public static final String CAMPAIGN_NAME = "TEST_CAMPAIGN";
    public static final String CHARACTER_NAME = "TEST_CHARACTER";
    public static final List<String> PLAYER_IDS = Arrays.asList(PLAYER_ID, "5e078469fc8fb856ec68fd99", "5e078469fc8fb856ec68fd99");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final List<String> FIRST_NAMES = Arrays.asList("Ragnor", "Dracca", "Corellion", "Thunk", "Garl", "Brevnor", "Vera");
    public static final List<String> LAST_NAMES = Arrays.asList("Stonebane", "Mannix", "Brightstar", "Goreman", "Lightfingers", "Pox", "Krinkles");

    public static String generateName(){
        Collections.shuffle(FIRST_NAMES);
        Collections.shuffle(LAST_NAMES);
        return FIRST_NAMES.get(0) + " " + LAST_NAMES.get(0);
    }

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

    public static Campaign buildCampaign() throws Exception{
       return Campaign
            .builder()
            .name("test_campaign")
            .startDate(DATE_FORMAT.parse("2000-1-11"))
            .endDate(DATE_FORMAT.parse("2100-2-22"))
            .playerIds(Arrays.asList("p10", "p11"))
            .characterIds(Arrays.asList("c12", "c13"))
            .npcIds(Arrays.asList("n14", "n15"))
            .monsterIds(Arrays.asList("m16", "m17"))
            .dmId("ptest")
            .description("description")
            .imageUrl("imgurl")
            .build();
    }

    public static Character buildCharacter(){
        return Character
            .builder()
            .name("test_character")
            .crace(CharacterRace.pickOne())
            .cclass(CharacterClass.pickOne())
            .level(1)
            .strength(TestUtils.generateAbility())
            .dexterity(TestUtils.generateAbility())
            .constitution(TestUtils.generateAbility())
            .wisdom(TestUtils.generateAbility())
            .intelligence(TestUtils.generateAbility())
            .charisma(TestUtils.generateAbility())
            .ctype(CharacterType.PC)
            .ac(generateAc())
            .hp(generateHp())
            .imageUrl("http://example.com/img1")
            .playerId("imgurl")
            .build();
    }

    public static Player buildPlayer(){
        return Player
            .builder()
            .name("test_player")
            .email("me@notu.com")
            .imageUrl("imgurl")
            .build();
    }

    public static Flux<Campaign> buildCampaignRepositoryTestCollection() throws Exception {
        Campaign campaign1 = buildCampaign();
        Campaign campaign2 = buildCampaign();
        Campaign campaign3 = buildCampaign();
        campaign1.setName(CAMPAIGN_NAME);
        campaign2.setPlayerIds(PLAYER_IDS);
        campaign3.setPlayerIds(PLAYER_IDS);
        return (Flux.just(campaign1, campaign2, campaign3, buildCampaign(), buildCampaign()));
    }

    public static Flux<Character> buildCharacterRepositoryTestCollection(){
        Character character1 = buildCharacter();
        Character character2 = buildCharacter();
        Character character3 = buildCharacter();
        character1.setPlayerId(PLAYER_ID);
        character2.setPlayerId(PLAYER_ID);
        character3.setName(CHARACTER_NAME);
        return(Flux.just(character1, character2, character3, buildCharacter(), buildCharacter()));
    }

    public static Flux<Player> buildPlayerRepositoryTestCollection(){
        return(Flux.just(buildPlayer(), buildPlayer(), buildPlayer(), buildPlayer(), buildPlayer()));
    }

}
